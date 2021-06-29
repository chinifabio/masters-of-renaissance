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

/**
 * This class represents the lite version of the PersonalBoard
 */
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

    /**
     * printer, che faccio?
     * @param amount
     */
    public void movePlayer(int amount) {
        this.track.movePlayer(amount);
    }

    /**
     * This method returns the player's position on the track
     * @return the player position
     */
    @JsonIgnore
    public Integer getPlayerPosition() {
        return this.track.getPlayerPosition();
    }

    /**
     * This method returns the warehouse of the player
     * @return the warehouse
     */
    public LiteWarehouse getWarehouse(){
        return this.warehouse;
    }

    /**
     * This method returns a depot, given its slot
     * @param slot of the required depot
     * @return the depot corresponding to the requested slot
     */
    @JsonIgnore
    public LiteDepot getDepot(DepotSlot slot) {
        return this.warehouse.getDepots(slot);
    }

    /**
     * This method set a depot into a given slot
     * @param slot DepotSlot to update
     * @param depot LiteDepot to insert
     */
    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.warehouse.setDepot(slot, depot);
    }

    /**
     * printer
     * @param popeTile
     */
    public void flipPopeTile(String popeTile) {
        this.track.flipPopeTile(popeTile);
    }

    /**
     * This method returns the map of PopeTiles
     * @return the map of PopeTiles
     */
    public HashMap<String, Boolean> getPopeTiles(){
        return this.track.getPopeTiles();
    }

    /**
     * This method updates the faith track with a new one
     * @param track the new LiteFaithTrack
     */
    public void updateFaithTrack(LiteFaithTrack track) {
        this.track = track;
    }

    /**
     * This method updates the DevCards possessed by the player.
     * @param devcards a map containing DevCardSlot and a list of LiteDevCard
     */
    public void setDevelop(HashMap<DevCardSlot, List<LiteDevCard>> devcards) {
        this.devCards = devcards;
    }

    /**
     * This method returns the DevCards of a player
     * @return the map containing DevCardSlot and a list of LiteDevCard
     */
    public HashMap<DevCardSlot, List<LiteDevCard>> getDevelop(){
        return this.devCards;
    }

    /**
     * This method sets a production
     * @param id ProductionID of the production
     * @param prod LiteProduction to set
     */
    public void setProduction(ProductionID id, LiteProduction prod) {
        this.warehouse.setProductions(id, prod);
    }

    /**
     * This method sets the possible discounts of the player
     * @param discounts list of LiteResources of discounts
     */
    public void setDiscounts(List<LiteResource> discounts) {
        this.discounts = discounts;
    }

    /**
     * This method sets the possible conversions of the player
     * @param collect list of MarbleColor of conversion
     */
    public void setConversions(List<MarbleColor> collect) {
        this.conversions = collect;
    }

    /**
     * This method returns the available discounts
     * @return the list of LiteResources of the discounts
     */
    public List<LiteResource> getDiscounts() {
        return new ArrayList<>(this.discounts);
    }

    /**
     * This method returns the available conversions
     * @return the list of MarbleColor of the conversions
     */
    public List<MarbleColor> getConversions() {
        return new ArrayList<>(this.conversions);
    }
}
