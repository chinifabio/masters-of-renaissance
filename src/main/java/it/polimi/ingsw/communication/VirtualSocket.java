package it.polimi.ingsw.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Time;
import java.util.*;

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
    private final Map<ChannelTypes, Object> waitingZone = new EnumMap<>(ChannelTypes.class);

    private final Object sendLocker = new Object();

    private final PrintStream sender;

    private boolean connected = true;

    /**
     * Create a virtual socket to sort received packet in channels
     * @param socket the real socket
     */
    public VirtualSocket(Socket socket) throws IOException {
        this.socket = socket;

        for (ChannelTypes ch : ChannelTypes.values()) {
            this.channelsQueue.put(ch, new LinkedList<>());
            this.waitingZone.put(ch, new Object());
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

        while (connected) {
            String serializedPacket = in.nextLine();
            try {
                Packet packet = new ObjectMapper()
                        .readerFor(Packet.class)
                        .readValue(serializedPacket);

                synchronized (waitingZone.get(packet.channel)) {
                    this.channelsQueue.get(packet.channel).add(packet);
                    waitingZone.get(packet.channel).notifyAll();
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

    /**
     * Return the last packet received in the passed channel
     * @param ch the channel where look at
     * @return the packet
     */
    public Packet pollPacketFrom(ChannelTypes ch, long timeout) {
        if(!connected) return new Packet(HeaderTypes.INVALID, ch, "connection closed");


        long time = System.currentTimeMillis();
        Packet result;
        synchronized (this.waitingZone.get(ch)) {
            while ((result = this.channelsQueue.get(ch).poll()) == null) {
                try {
                    this.waitingZone.get(ch).wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (System.currentTimeMillis() - time > timeout) return new Packet(HeaderTypes.INVALID, ch, "connection closed");

                if (!connected) return new Packet(HeaderTypes.INVALID, ch, "connection closed");
            }
        }
        return result;
    }

    /**
     * notify all the listening thread for a packet
     */
    public void disconnect() {
        this.connected = false;

        for (ChannelTypes ch : ChannelTypes.values()) {
            synchronized (this.waitingZone.get(ch)) {
                this.waitingZone.get(ch).notifyAll();
            }
        }
    }
}
