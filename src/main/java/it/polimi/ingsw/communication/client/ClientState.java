package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

public abstract class ClientState {

    /**
     * represent the client state and contains every method that a state must implement
     */
    protected final Client context;

    /**
     * This attribute is set by a subclasses to put a message in the player state exception
     */
    private final String errorMessage;

    public ClientState(Client context, String eMes) {
        this.context = context;
        this.errorMessage = eMes;
    }

    /**
     * Set the player's nickname
     * @return the result of the operation
     */
    public Packet setNickname(){
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    public Packet setNumberOfPlayers(){ return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage); }

    public Packet buyDevCard(){ return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage); }

    public Packet discardLeader(){ return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage); }

    public Packet useMarketTray(){ return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage); }

    public Packet paintMarbleColor(){ return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage); }

    //mancano le altre funzioni: muovere risorse nei depot/prod, attivare la produzione
}
