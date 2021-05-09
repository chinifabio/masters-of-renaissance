package it.polimi.ingsw.view.litemodel;

import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LitePersonalBoard {

    /**
     * This attribute is the representation of the FaithTrack
     */
    private final LiteFaithTrack track = new LiteFaithTrack();

    /**
     * Used to store information of the warehouse
     */
    private final LiteWarehouse warehouse = new LiteWarehouse();

    /**
     * Used to save leader cards
     */
    private List<String> leaderCards;

    /**
     * Used to memorize dev cards and their position
     */
    private Map<DevCardSlot, String> devCard = new EnumMap<>(DevCardSlot.class);

    public void setLeader(List<String> cards) {
        // todo now use strings, then we will build the associated card in lite model
        this.leaderCards = cards;
    }

    public List<String> getLeaderCards() {
        return leaderCards;
    }

    /**
     * This method return the new status of the FaithTrack
     * @return (LiteFaithTrack) faithTrack
     */
    public LiteFaithTrack getTrack(){
        return this.track;
    }

    public void movePlayer(int amount) {
        this.track.movePlayer(amount);
    }

    public Integer getPlayerPosition() {
        return this.track.getPlayerPosition();
    }

    public LiteDepot getDepot(DepotSlot slot) {
        return this.warehouse.getDepots(slot);
    }

    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.warehouse.setDepot(slot, depot);
    }
}
