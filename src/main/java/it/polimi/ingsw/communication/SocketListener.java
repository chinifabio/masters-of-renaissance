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

public class SocketListener implements Runnable {
    /**
     * The real socket on which sort packets in channels
     */
    private final Socket socket;

    private boolean connected;
    private final int timeout = 10000;

    private final Queue<Packet> packetQueue = new LinkedList<>();

    private final PrintStream sender;
    private final Scanner receiver;

    private final Timer timer = new Timer();
    private final Disconnectable disconnectable;

    /**
     * Create a virtual socket to sort received packet in channels
     * @param socket the real socket
     */
    public SocketListener(Socket socket, Disconnectable disconnectable) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(timeout);

        this.disconnectable = disconnectable;

        sender = new PrintStream(socket.getOutputStream());
        receiver = new Scanner(socket.getInputStream());

        timer.scheduleAtFixedRate(new PingerTask(this), 0, timeout - 2000);
        connected = true;
    }

    /**
     * Send the paket passed
     * @param packet the packet to send
     */
    public void send(Packet packet) {
        if (!connected) return;
        synchronized (sender) {
            String serialized;
            try {
                serialized = new ObjectMapper().writeValueAsString(packet);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.out.println("serialization error, abort operation...");
                return;
            }

            sender.println(serialized);
        }
    }

    public Packet pollPacket() {
        if(!connected) return new Packet(HeaderTypes.INVALID, ChannelTypes.CONNECTION_STATUS, "connection closed");

        Packet result;
        synchronized (receiver) {
            while ((result = packetQueue.poll()) == null) {
                try {
                    receiver.wait();
                } catch (InterruptedException e) {
                    disconnectable.handleDisconnection();
                }

                if (!connected) return new Packet(HeaderTypes.TIMEOUT, ChannelTypes.CONNECTION_STATUS, "connection closed");
                receiver.notifyAll();
            }
        }

        return result;
    }

    /**
     * Receive a packet and give it to the listener
     */
    @Override
    public void run() {
        String serializedPacket;
        while (connected) {
            try {
                serializedPacket = receiver.nextLine();
            } catch (NoSuchElementException timeout) {
                disconnectable.handleDisconnection();
                return;
            }

            try {
                Packet received = new ObjectMapper()
                        .readerFor(Packet.class)
                        .readValue(serializedPacket);

                synchronized (receiver) {
                    packetQueue.add(received);
                    receiver.notifyAll();
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.out.println("invalid packet received");
            }
        }
    }
}

class PingerTask extends TimerTask {
    private final SocketListener socket;

    public PingerTask(SocketListener socket) {
        this.socket = socket;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        socket.send(new Packet(HeaderTypes.PING, ChannelTypes.CONNECTION_STATUS, "I'm in, bitch"));
    }
}
