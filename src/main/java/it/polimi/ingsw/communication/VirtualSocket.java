package it.polimi.ingsw.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VirtualSocket implements Runnable{
    private final Socket socket;

    private final Map<ChannelTypes, Queue<Packet>> channelsQueue = new EnumMap<>(ChannelTypes.class);

    private final List<ChannelTypes> channelList = new ArrayList<>(Arrays.asList(ChannelTypes.values()));

    public VirtualSocket(Socket socket) {
        this.socket = socket;

        for (ChannelTypes ch : ChannelTypes.values()) {
            this.channelsQueue.put(ch, new LinkedList<>());
        }
    }

    /**
     * Receive a packet and give it to the listener
     */
    @Override
    public void run() {
        Scanner in = null;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while(true) {
            Packet packet = null;
            try {
                packet = new ObjectMapper()
                        .readerFor(Packet.class)
                        .readValue(in.nextLine());
                this.channelsQueue.get(packet.channel).add(packet);
                packet.channel.notifyAll();
            } catch (JsonProcessingException e) {
                System.out.println("invalid packet received");
            }
        }
    }

    /**
     * return all the possible channel type to get in queue for new packets
     * @return a new list of channel available
     */
    public List<ChannelTypes> getChannelsList() {
        List<ChannelTypes> clone = new ArrayList<>();
        for(ChannelTypes ch : channelList) clone.add(ch);
        return clone;
    }

    public synchronized void send(Packet packet) {
        String serialized = null;
        try {
            serialized = new ObjectMapper().writeValueAsString(packet);
        } catch (JsonProcessingException e) {
            System.out.println("serialization error, than abort server operation");
            return;
        }

        try {
            this.socket.getOutputStream().write(serialized.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("can't find socket");
        }
    }

    /**
     * return the last packet received in the passed channel
     * @param ch the channel where look at
     * @return the packet
     */
    public synchronized Packet pollPacketFrom(ChannelTypes ch) {
        return this.channelsQueue.get(ch).poll();
    }
}
