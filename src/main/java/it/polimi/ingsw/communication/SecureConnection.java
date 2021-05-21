package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SecureConnection {
    private static final long timeOut = 4000;

    private static final Packet ping = new Packet(HeaderTypes.PING, ChannelTypes.CONNECTION_STATUS, "I'm in, bitch");
    private static final Packet pong = new Packet(HeaderTypes.PONG, ChannelTypes.CONNECTION_STATUS, "Al right, keep pinging me");

    public static void pinger(Disconnectable thread) {
        VirtualSocket socket = thread.disconnectableSocket();

        Timer timer = new Timer();
        TimerTask sendPing = new TimerTask() {
            @Override
            public void run() {
                socket.send(ping);

                if (socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS).header != HeaderTypes.PONG) {
                    thread.handleDisconnection();
                }
            }
        };

        timer.scheduleAtFixedRate(sendPing, 0, timeOut);
    }

    public static void ponger(Disconnectable thread) {
        VirtualSocket socket = thread.disconnectableSocket();

        Timer timer = new Timer();
        TimerTask sendPong = new TimerTask() {
            @Override
            public void run() {
                if (socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS).header != HeaderTypes.PING) {
                    thread.handleDisconnection();
                    return;
                }

                socket.send(pong);
            }
        };

        timer.scheduleAtFixedRate(sendPong, 0, timeOut);
    }
}
