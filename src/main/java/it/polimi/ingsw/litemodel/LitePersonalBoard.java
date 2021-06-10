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

import java.util.*;

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
    private List<LiteLeaderCard> leaderCards = new ArrayList<>();

    /**
     * Used to memorize dev cards and their position
     */
    private HashMap<DevCardSlot, List<LiteDevCard>> devCards;

    private List<LiteResource> discounts = new ArrayList<>();
    private List<MarbleColor> conversions = new ArrayList<>();

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


    public HashMap<String, Boolean> getPopeTiles(){
        return this.track.getPopeTiles();
    }

    public void updateFaithTrack(LiteFaithTrack track) {
        this.track = track;
    }

    public void setDevelop(HashMap<DevCardSlot, List<LiteDevCard>> devcards) {
        this.devCards = devcards;
    }

    public HashMap<DevCardSlot, List<LiteDevCard>> getDevelop(){
        return this.devCards;
    }

    public void setProduction(ProductionID id, LiteProduction prod) {
        this.warehouse.setProductions(id, prod);
    }

    public void setDiscounts(List<LiteResource> discounts) {
        this.discounts = discounts;
    }

    public void setConversions(List<MarbleColor> collect) {
        this.conversions = collect;
    }

    public List<LiteResource> getDiscounts() {
        return new ArrayList<>(this.discounts);
    }

    public List<MarbleColor> getConversions() {
        return new ArrayList<>(this.conversions);
    }
}
