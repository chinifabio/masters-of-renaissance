package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

public class DisconnectedPlayerState extends PlayerState {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context is the context
     */
    protected DisconnectedPlayerState(Player context) {
        super(context, "disconnected, you aren't able to do stuff (◉᷅_◉᷅ )",
                new Packet(HeaderTypes.RECONNECTED, ChannelTypes.PLAYER_ACTIONS, "reconnect"));
    }

    /**
     * Receive the input to start the turn
     */
    @Override
    public void starTurn() {
        this.context.match.turnDone();
    }

    /**
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState reconnectionState() {
        return this;
    }

    /**
     * can the player do stuff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return false;
    }
}
