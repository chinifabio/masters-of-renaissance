package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.WrongDepotException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.PopeTile;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Bucket;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class identify the Player's PersonalBoard
 */
public class PersonalBoard {

    /**
     * This attribute is the list of the available productions that the Player could activates
     */
    private Map<ProductionID, Production> availableProductions;

    /**
     * This attribute is the list of the available Discount applied when the player buys DevCard
     */
    private List<ResourceRequisite> availableDiscount;

    /**
     * This attribute is the list of the available resources in the Warehouse
     */
    private List<ResourceRequisite> availableResources;

    /**
     * This attribute is the Deck of the LeaderCards owned by the Player
     */
    private Deck<LeaderCard> leaderDeck;

    /**
     * This attribute is the Deck of the DevCards owned by the Player and their position in the PersonalBoard
     */
    private Map<DevCardSlot, Deck<DevCard>> devDeck;

    /**
     * This attribute contains the function that maps MarketMarble to their respective Resources
     */
    private Function<MarbleColor, ResourceRequisite> marbleConversion;

    /**
     * This attribute is the Warehouse contained into the PersonalBoard
     */
    private Warehouse warehouse;

    /**
     * This attribute is the FaithTrack of the PersonalBoard
     */
    private FaithTrack faithTrack;


    /**
     * This method is the constructor of the class
     */
    public PersonalBoard() {
        this.availableProductions = new EnumMap<>(ProductionID.class);
        this.availableDiscount = new ArrayList<>();
        this.availableResources = new ArrayList<>();
        this.leaderDeck = new Deck<>();
        this.devDeck = new EnumMap<>(DevCardSlot.class);
        devDeck.put(DevCardSlot.LEFT,new Deck<>());
        devDeck.put(DevCardSlot.CENTER,new Deck<>());
        devDeck.put(DevCardSlot.RIGHT,new Deck<>());
        //TODO Da guardare meglio
        this.marbleConversion = marbleColor -> null;
        this.warehouse = new Warehouse();
        this.faithTrack = new FaithTrack();
    }

    /**
     * This method add a Discount into the list of availableDiscount
     */
    public void addDiscount(){}

    /**
     * This method add the DevCard bought by the Player into the DevCardSlot
     * @param slot is the slot where the DevCard is inserted
     * @param card is the DevCard bought by the Player
     */
    public void addDevCard(DevCardSlot slot, DevCard card){}

    /**
     * This method add the LeaderCard in the Player's PersonalBoard
     * @param card is the LeaderCard that the Player has chosen
     */
    public void addLeaderCard(LeaderCard card){}

    /**
     * This method activate the SpecialAbility of the LeaderCard
     * @param card is the LeaderCard
     */
    public void activateLeaderCard(String card){}

    /**
     * This method remove the LeaderCard from Player's PersonalBoard
     * @param card is the card to be removed
     */
    public void discardLeaderCard(String card){}

    /**
     * This method convert the MarketMarble into their respective Resources
     * @param func is the function that do the conversion
     */
    public void MarbleConversion(Function<MarbleColor, ResourceRequisite> func){}

    /**
     * This method add a new Production into the list of availableProductions
     * @param prod is the Production to add
     */
    public void addProduction(Production prod){
    }

    /**
     * This method select the production that the Player wants to activate
     * @param productionID is the identifier of the Production that will be activated
     */
    public void selectProduction(ProductionID productionID){
    }

    /**
     * This method activate the productions selected by the Player
     */
    public void activateProductions(){
    }

    /**
     * This method moves resources from a Depot to another one
     * @param from is the Depot where the resources are taken from
     * @param to is the Depot where the resources will be stored
     * @param resource is the resource to move
     * @return true if the resources are correctly moved
     * @throws NegativeResourcesDepotException if the Depot "from" hasn't enough resources to move
     * @throws WrongDepotException if the Depot "from" is empty or doesn't have the same type of resources of "resource"
     */
    public void moveResourceDepot(DepotSlot from, DepotSlot to, Resource resource) throws WrongDepotException, NegativeResourcesDepotException {
        warehouse.moveBetweenDepot(from,to, resource);
    }


    //Implementare MoveBetweenBucket per spostare le risorse dai Depot alla Produzione (moveInProduction)
    public void moveBetweenBucket(Bucket from, Bucket to, Resource resource) throws WrongDepotException {
    }

    /**
     * tells to the faith track to move amount times the player marker
     * @param amount the amount to move
     */
    public void moveFaithMarker(int amount) {
        //TODO implementation
    }

    /**
     * create a new depot in the warehouse
     * @param depot the new depot
     */
    public void addDepot(Depot depot) {
        // TODO implementazione
    }

    /**
     * Move the Lorenzo marker amount times
     * @param amount moves of lorenzo
     */
    public void moveLorenzo(int amount) {
        // TODO implementazione
    }

    /**
     * return all the available production
     * @return list of produciton
     */
    public List<Production> possibleProduction() {
        // TODO implementazione
        return null;
    }

    /**
     * return all the leader card of the player packed in a deck
     * @return deck of leader card
     */
    public Deck<LeaderCard> viewLeaderCard() {
        // TODO implementazione
        return null;
    }

    /**
     * return all the resources that the player has. It doesn't matter the depot in which they are stored
     * @return list of resources
     */
    public List<Resource> viewResources() {
        // TODO implementazione
        return null;
    }

    /**
     * return a map of the top develop card placed in the player board decks
     * @return a map of devCardSlot - DevCard
     */
    public Map<DevCardSlot, DevCard> viewDevCards() {
        //TODO implementazione
        return null;
    }

    /**
     * return the faith marker position of the player
     *
     * @return faith marker position of the player
     */
    public int FaithMarkerPosition() {
        // TODO implementazione
        return 0;
    }

    /**
     * check if it need to be flipped the pope tile passed as parameter
     * @param popeTile the pope tile to check
     */
    public void flipPopeTile(PopeTile popeTile) {
        // TODO implementazione
    }

    /**
     * store the resource in the buffer depot, then it will be the player to move
     * from buffer depot to a legal one
     * @param resource the resource obtained
     */
    public void obtainResource(Resource resource) {
    }
}
