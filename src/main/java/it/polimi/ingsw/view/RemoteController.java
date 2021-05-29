package it.polimi.ingsw.view;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.Packet;

public class RemoteController {
    private final VirtualSocket socket;
    private final View view;

    public RemoteController(VirtualSocket socket, View view) {
        this.socket = socket;
        this.view = view;
    }

    public void handlePacker(Packet sending) {
        if(sending.channel != ChannelTypes.PLAYER_ACTIONS) {
            view.notifyPlayerError("Wrong channel");
            return;
        }

        socket.send(sending);
        view.notifyPlayer(socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS).body);
    }


}
