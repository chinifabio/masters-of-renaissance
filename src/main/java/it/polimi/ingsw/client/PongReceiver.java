package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.Packet;

public class PongReceiver implements Runnable{

    public final VirtualSocket socket;

    public final ClientPing ref;

    public PongReceiver(VirtualSocket socket, ClientPing ref) {
        this.socket = socket;
        this.ref = ref;
    }

    @Override
    public void run() {
        while(true){
            Packet pong;
            synchronized (ref.pong){
                pong = this.socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS);
                this.ref.setPong(pong);
            }
        }
    }
}
