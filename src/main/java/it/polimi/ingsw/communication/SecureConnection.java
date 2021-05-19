package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

public class SecureConnection {
    private static final long timeOut = 3000;

    private static final Packet ping = new Packet(HeaderTypes.PING, ChannelTypes.CONNECTION_STATUS, "I'm in, bitch");
    private static final Packet pong = new Packet(HeaderTypes.PONG, ChannelTypes.CONNECTION_STATUS, "Al right, keep pinging me");

    public static Thread pinger(Disconnectable thread) {
        return new Thread(()->{
            VirtualSocket socket = thread.disconnectableSocket();

            while (true) {
                socket.send(ping);

                if (socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS, timeOut).header != HeaderTypes.PONG) {
                    socket.disconnect();
                    thread.handleDisconnection();
                    return;
                }

                try {
                    Thread.sleep(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    socket.disconnect();
                    thread.handleDisconnection();
                    System.out.println("can't wait :(");
                    return;
                }
            }
        });
    }

    public static Thread ponger(Disconnectable thread) {
        return new Thread(()->{
            VirtualSocket socket = thread.disconnectableSocket();

            while (true) {
                if (socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS, timeOut).header != HeaderTypes.PING) {
                    socket.disconnect();
                    thread.handleDisconnection();
                    return;
                }
                socket.send(pong);

                try {
                    Thread.sleep(timeOut - 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    socket.disconnect();
                    thread.handleDisconnection();
                    return;
                }
            }
        });
    }
}
