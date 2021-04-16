package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.WrongDepotException;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.exceptions.productionException.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;

import java.util.*;
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
     * This attribute maps the ProductionID to the DevCardSlot
     */
    private Map<DevCardSlot, ProductionID> productionSlotMap;

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
     * This attribute is the Player that own this PersonalBoard
     */
    private Player player;


    /**
     * This method is the constructor of the class
     */
    public PersonalBoard(Player player) throws IllegalTypeInProduction {
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
        this.warehouse = new Warehouse(player);
        this.faithTrack = new FaithTrack();
        this.player = player;
    }

    /**
     * This method add a Discount into the list of availableDiscount
     */
    public void addDiscount(){
        //TODO it's probably better if the player has them
    }

    /**
     * This method add the DevCard bought by the Player into the DevCardSlot
     * @param slot is the slot where the DevCard is inserted
     * @param card is the DevCard bought by the Player
     */
    public boolean addDevCard(DevCardSlot slot, DevCard card){
        boolean result = false;
        Deck<DevCard> temp = this.devDeck.get(slot);
        if(temp == null) temp = new Deck<>();

        // queste due righe sono inutili se nel costruttore creo i devDeck, ancora da decidere cosa fare

        if(checkDevCard(slot,card)){
            try {
                temp.insertCard(card);
                result=true;
                this.devDeck.put(slot,temp);
            } catch (AlreadyInDeckException e) {          // it should not be possible to enter this catch, the level is different -> can't have two cards with same ID
                System.out.println(e.getMsg() + "big problem, this should be an unreachable statement");
            }
        }
        return result;
    }

    /**
     * This method checks if a card can be inserted into a given position in the playerBoard.
     * @param slot where the card will be inserted.
     * @param card the card that will be inserted.
     * @return true if the card can be placed into that position.
     */
    public boolean checkDevCard(DevCardSlot slot, DevCard card) {
        boolean result = false;
        Deck<DevCard> temp = this.devDeck.get(slot);
        if(temp == null) temp = new Deck<>();

        try {
            if (temp.peekFirstCard().getLevel().getLevelCard() == (card.getLevel().getLevelCard() - 1)) result = true;
        } catch (EmptyDeckException e) {
            if (card.getLevel() == LevelDevCard.LEVEL1) result = true;
        }
        return result;
        }


    /**
     * return a map of the top develop card placed in the player board decks
     * @return a map of devCardSlot - DevCard
     */
    public Map<DevCardSlot, DevCard> viewDevCards() {
        Map<DevCardSlot, DevCard> tempMap = new EnumMap<>(DevCardSlot.class);
        for(DevCardSlot devCardSlot : DevCardSlot.values()){
            try {
                tempMap.put(devCardSlot,this.devDeck.get(devCardSlot).peekFirstCard());
            } catch (EmptyDeckException e) {
                System.out.println(e.getMsg() + "; no " + devCardSlot + " production.");  // needs some changes
            }
        }
        return tempMap;
    }


    /**
     * This method add the LeaderCard in the Player's PersonalBoard
     * @param card is the LeaderCard that the Player has chosen
     */
    public void addLeaderCard(LeaderCard card){
        try {
            this.leaderDeck.insertCard(card);
        } catch (AlreadyInDeckException e) {
            System.out.println(e.getMsg());
        }
    }

    /**
     * This method activate the SpecialAbility of the LeaderCard
     * @param card is the LeaderCard
     */
    public void activateLeaderCard(String card){    //TODO implementation - effect
        try {
            this.leaderDeck.peekCard(card).activate();
        } catch (MissingCardException e) {
            System.out.println(e.getMsg());
        }
    }

    /**
     * This method remove the LeaderCard from Player's PersonalBoard
     * @param card is the card to be removed
     */
    public void discardLeaderCard(String card){
        try {
            this.leaderDeck.discard(card);
        } catch (EmptyDeckException e) {
            System.out.println(e.getMsg() + "il deck è vuoto");
        } catch (MissingCardException e) {
            System.out.println(e.getMsg() + "non c'è leader card con questo id");
        }
    }

    /**
     * return all the leader card of the player packed in a deck
     * @return deck of leader card
     */
    public Deck<LeaderCard> viewLeaderCard() {
        return this.leaderDeck;
    }

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
        //TODO implementation
    }

    /**
     * return all the available production
     * @return list of produciton
     */
    public List<Production> possibleProduction() {
        List<Production> temp = new ArrayList<>();
        for(ProductionID productionID : ProductionID.values()){
            temp.add(this.availableProductions.get(productionID));
        }
        return temp;
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
     * store the resource in the buffer depot, then it will be the player to move
     * from buffer depot to a legal one
     * @param resource the resource obtained
     */
    public void obtainResourcePBoard(Resource resource) {
        try {
            this.warehouse.insertInDepot(DepotSlot.BUFFER,resource);
        } catch (UnobtainableResourceException e) {
            System.out.println(e.getMsg());
        }
    }


    //TODO not sure if needed.
    /**
     * return all the resources that the player has. It doesn't matter the depot in which they are stored
     * @return list of resources
     */
    public List<Resource> viewResources() {
        List<Resource> temp;
        temp = this.warehouse.viewResourcesInStrongbox();
        for(DepotSlot depotSlot : DepotSlot.values()) {
            if(!(depotSlot == DepotSlot.STRONGBOX) && !(depotSlot == DepotSlot.BUFFER)) {
                try {
                    temp.add(this.warehouse.viewResourcesInDepot(depotSlot));
                }
                catch (NullPointerException e){
                    System.out.println("Il depot " + depotSlot + " non esiste.");
                }
            }
        }
        return temp;
    }

    /**
     * return the resources that the player has in the specified depot
     * @return list of resources
     */
    public List<Resource> askResource(DepotSlot depotSlot) {
        if(depotSlot == DepotSlot.STRONGBOX){
            return this.warehouse.viewResourcesInStrongbox();
        }
        else{
            List<Resource> temp = new ArrayList<>();
            temp.add(warehouse.viewResourcesInDepot(depotSlot));
            return temp;
        }
    }

    /**
     * create a new depot in the warehouse
     * @param depot the new depot
     */
    public void addDepot(Depot depot) {
        //    try {
        //        warehouse.addDepot(depot);
        //    } catch (ExtraDepotsException e) {
        //        System.out.println(e.getMsg());
        //    }
    }

    /**
     * This method moves resources from a Depot to another one
     * @param from is the Depot where the resources are taken from
     * @param to is the Depot where the resources will be stored
     * @param resource is the resource to move
     * @throws NegativeResourcesDepotException if the Depot "from" hasn't enough resources to move
     * @throws WrongDepotException if the Depot "from" is empty or doesn't have the same type of resources of "resource"
     */
    public void moveResourceDepot(DepotSlot from, DepotSlot to, Resource resource) throws WrongDepotException, NegativeResourcesDepotException, UnobtainableResourceException {
        warehouse.moveBetweenDepot(from,to, resource);
    }

    /**
     * tells to the faith track to move amount times the player marker.
     * @param amount the amount to move.
     * @return true if the move is allowed, false otherwhise.
     */
    public boolean moveFaithMarker(int amount) {
        boolean result = false;
        try {
            this.faithTrack.movePlayer(amount);
            result = true;
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        } catch (WrongPointsException e) {
            System.out.println(e.getMsg());
        }
        return result;
    }

    /**
     * return the faith marker position of the player
     * @return faith marker position of the player
     */
    public int FaithMarkerPosition() {
        return faithTrack.getPlayerPosition();
    }

    /**
     * check if it need to be flipped the pope tile passed as parameter
     * @param popeTile the pope tile to check
     */
    public void flipPopeTile(VaticanSpace popeTile) {
        this.faithTrack.flipPopeTile(popeTile);
    }

    /**
     * store the resource in the buffer depot, then it will be the player to move
     * from buffer depot to a legal one
     * @param resource the resource obtained
     */
    public void obtainResource(Resource resource) {
    }
}
