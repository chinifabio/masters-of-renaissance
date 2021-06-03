package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

public class BuyDevCardPlayerState extends PlayerState{

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context is the context
     */
    protected BuyDevCardPlayerState(Player context) {
        super(context, "buy dev card phase",
                new Packet(HeaderTypes.RECONNECTED, ChannelTypes.PLAYER_ACTIONS, "reconnect"));
    }

    /**
     * can the player do stuff?
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return true;
    }

    /**
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState reconnectionState() {
        return new NotHisTurnPlayerState(this.context);
    }

// -------------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------


    @Override
    public Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        try {
            this.context.personalBoard.moveResourceDepot(from, to, loot);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    public Packet cancel(){
        //rollback delle azioni
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }
}
