package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteFaithTrack;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.litemodel.litewarehouse.LiteWarehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LitePersonalBoard {

    /**
     * This attribute is the representation of the FaithTrack
     */
    private LiteFaithTrack track = new LiteFaithTrack();

    /**
     * Used to store information of the warehouse
     */
    private final LiteWarehouse warehouse = new LiteWarehouse();

    /**
     * Used to save leader cards
     */
    private List<LiteLeaderCard> leaderCards;

    /**
     * Used to memorize dev cards and their position
     */
    private final Map<DevCardSlot, String> devCard = new EnumMap<>(DevCardSlot.class);

    private List<LiteResource> discounts;

    private List<MarbleColor> conversions;

    public void setLeader(List<LiteLeaderCard> cards) {
        this.leaderCards = cards;
    }

    public List<LiteLeaderCard> getLeaderCards() {
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

    @JsonIgnore
    public Integer getPlayerPosition() {
        return this.track.getPlayerPosition();
    }

    public LiteWarehouse getWarehouse(){
        return this.warehouse;
    }

    @JsonIgnore
    public LiteDepot getDepot(DepotSlot slot) {
        return this.warehouse.getDepots(slot);
    }

    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.warehouse.setDepot(slot, depot);
    }

    public void flipPopeTile(String popeTile) {
        this.track.flipPopeTile(popeTile);
    }

    @JsonIgnore
    public Map<String, Boolean> getPopeTiles(){
        return this.track.getPopeTiles();
    }

    public void updateFaithTrack(LiteFaithTrack track) {
        this.track = track;
    }

    public void setDevelop(List<LiteDevCard> deck) {
        // todo
    }

    public void setProduction(ProductionID id, LiteProduction prod) {
        this.warehouse.setProduction(id, prod);
    }

    public void setDiscounts(List<LiteResource> discounts) {
        this.discounts = discounts;
    }

    public void setConversions(List<MarbleColor> collect) {
        this.conversions = collect;
    }
}
