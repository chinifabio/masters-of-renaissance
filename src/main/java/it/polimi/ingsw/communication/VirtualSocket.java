package it.polimi.ingsw.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This virtual socket class sorts received packet in channels as packet queue
 */
public class VirtualSocket implements Runnable{
    /**
     * The real socket on which sort packets in channels
     */
    private final Socket socket;

    /**
     * All the queue in which save packets received
     */
    private final Map<ChannelTypes, Queue<Packet>> channelsQueue = new EnumMap<>(ChannelTypes.class);

    /**
     * All the object to lock and wait
     */
    // Object must not be modified
    private final Map<ChannelTypes, AtomicBoolean> waitingZone = new EnumMap<>(ChannelTypes.class);

    private final Object sendLocker = new Object();

    private final PrintStream sender;

    private boolean connected = true;

// securing connection

    private final int timeout = 10000;

    Timer timer = new Timer();

    private DisconnectionNotifier notifier;

    /**
     * Create a virtual socket to sort received packet in channels
     * @param socket the real socket
     */
    public VirtualSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(timeout);

        for (ChannelTypes ch : ChannelTypes.values()) {
            this.channelsQueue.put(ch, new LinkedList<>());
            this.waitingZone.put(ch, new AtomicBoolean(true));
        }

        this.sender = new PrintStream(this.socket.getOutputStream());
    }

    /**
     * Receive a packet and give it to the listener
     */
    @Override
    public void run() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String serializedPacket;

        while (connected) {
            try {
                serializedPacket = in.nextLine();
            } catch (NoSuchElementException timeout) {
                disconnect();
                return;
            }

            try {
                Packet packet = new ObjectMapper()
                        .readerFor(Packet.class)
                        .readValue(serializedPacket);

                if (waitingZone.get(packet.channel).get()){
                    synchronized (waitingZone.get(packet.channel)) {
                        this.channelsQueue.get(packet.channel).add(packet);
                        waitingZone.get(packet.channel).notifyAll();
                    }
                } else {
                    send(new Packet(HeaderTypes.INVALID, packet.channel, "Wait, inhibited channel"));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.out.println("invalid packet received");
            }
        }
    }

    /**
     * Send the paket passed
     * @param packet the packet to send
     */
    public void send(Packet packet) {
        if (!connected) return;

        synchronized (this.sendLocker) {
            String serialized;
            try {
                serialized = new ObjectMapper().writeValueAsString(packet);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.out.println("serialization error, abort operation...");
                return;
            }

            this.sender.println(serialized);
        }
    }

    /**
     * Return the last packet received in the passed channel
     * @param ch the channel where look at
     * @return the packet
     */
    public Packet pollPacketFrom(ChannelTypes ch) {
        if(!connected) return new Packet(HeaderTypes.INVALID, ch, "connection closed");

        Packet result;
        synchronized (this.waitingZone.get(ch)) {
            while ((result = this.channelsQueue.get(ch).poll()) == null) {
                try {
                    this.waitingZone.get(ch).wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!connected) return new Packet(HeaderTypes.TIMEOUT, ch, "connection closed");
            }
        }
        return result;
    }

    public void pinger(Disconnectable disconnectable) {
        Pinger pinger = new Pinger(this, disconnectable);
        notifier = pinger;
        timer.scheduleAtFixedRate(pinger, 0, timeout - 2000);
    }

    public void ponger(Disconnectable disconnectable) {
        Ponger ponger = new Ponger(this, disconnectable);
        notifier = ponger;
        timer.scheduleAtFixedRate(ponger, 0, timeout - 2000);
    }

    /**
     * notify all the listening thread for a packet
     */
    public void disconnect() {
        this.connected = false;
        timer.cancel();
        notifier.notifyDisconnection();

        for (ChannelTypes ch : ChannelTypes.values()) {
            synchronized (this.waitingZone.get(ch)) {
                this.waitingZone.get(ch).notifyAll();
            }
        }
    }
}

interface DisconnectionNotifier {
    void notifyDisconnection();
}

class Ponger extends TimerTask implements DisconnectionNotifier {
    private final VirtualSocket socket;
    private final Disconnectable thread;

    public Ponger(VirtualSocket socket, Disconnectable thread) {
        this.socket = socket;
        this.thread = thread;
    }

    @Override
    public void notifyDisconnection() {
        thread.handleDisconnection();
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        if (socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS).header != HeaderTypes.PING) {
            thread.handleDisconnection();
            return;
        }

        socket.send(new Packet(HeaderTypes.PONG, ChannelTypes.CONNECTION_STATUS, "Al right, keep pinging me"));
    }
}

class Pinger extends TimerTask implements DisconnectionNotifier {
    private final VirtualSocket socket;
    private final Disconnectable thread;

    public Pinger(VirtualSocket socket, Disconnectable thread) {
        this.socket = socket;
        this.thread = thread;
    }

    @Override
    public void notifyDisconnection() {
        thread.handleDisconnection();
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        socket.send(new Packet(HeaderTypes.PING, ChannelTypes.CONNECTION_STATUS, "I'm in, bitch"));

        if (socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS).header != HeaderTypes.PONG) {
            thread.handleDisconnection();
        }
    }
}
