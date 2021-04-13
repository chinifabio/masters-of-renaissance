package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.WrongDepotException;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
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
    public boolean addDevCard(DevCardSlot slot, DevCard card){
        Deck<DevCard> temp = this.devDeck.remove(slot);
        boolean result = false;
        if(temp == null) temp = new Deck<>();

        // queste due righe sono inutili se nel costruttore creo i devDeck, ancora da decidere cosa fare

        try {
            System.out.println(temp.peekFirstCard().getLevel().getLevelCard()+","+(card.getLevel().getLevelCard()-1));
            if(temp.peekFirstCard().getLevel().getLevelCard() == (card.getLevel().getLevelCard()-1)) {
                try {
                    temp.insertCard(card);
                    result=true;
                } catch (AlreadyInDeckException e) {          // it should not be possible to enter this catch, the level is different -> can't have two cards with same ID
                    System.out.println(e.getMsg());
                }
            }
            else{
            }
        } catch (EmptyDeckException e1) {
            if(card.getLevel() == LevelDevCard.LEVEL1){
                try {
                    temp.insertCard(card);
                    result = true;
                } catch (AlreadyInDeckException e2) {         // it should not be possible to enter this catch, empty deck -> can't have two cards with same ID
                    System.out.println(e2.getMsg());
                }
            }
        }
        finally {
            this.devDeck.put(slot,temp);
            return result;
        }
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
     * @throws NegativeResourcesDepotException if the Depot "from" hasn't enough resources to move
     * @throws WrongDepotException if the Depot "from" is empty or doesn't have the same type of resources of "resource"
     */
    public void moveResourceDepot(DepotSlot from, DepotSlot to, Resource resource) throws WrongDepotException, NegativeResourcesDepotException {
        warehouse.moveBetweenDepot(from,to, resource);
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
     * @param resource the new depot
     */
    public void addDepot(Depot depot) {
    //    try {
    //        warehouse.addDepot(resource);
    //    } catch (ExtraDepotsException e) {
    //        System.out.println(e.getMsg());
    //    }
    }

    /**
     * Move the Lorenzo marker amount times
     * @param amount moves of lorenzo
     */
    public void moveLorenzo(int amount) {
        try {
            faithTrack.moveLorenzo(amount);
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        } catch (WrongPointsException e) {
            System.out.println(e.getMsg());
        }
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
        Deck<LeaderCard> temp = this.leaderDeck;
        return temp;
    }

    /**
     * return all the resources that the player has. It doesn't matter the depot in which they are stored
     * @return list of resources
     */
    public List<Resource> viewResources() {
        List<Resource> temp;
        temp = warehouse.viewResourcesInStrongbox(DepotSlot.STRONGBOX);
        for(DepotSlot depotSlot : DepotSlot.values()) {
            if(!(depotSlot == DepotSlot.STRONGBOX) && !(depotSlot == DepotSlot.BUFFER)) {
                try {
                    temp.add(warehouse.viewResourcesInDepot(depotSlot));
                }
                catch (NullPointerException e){
                    System.out.println("Il depot " + depotSlot + " non esiste.");
                }
            }
        }
        return temp;
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
                System.out.println(e.getMsg() + "; no " + devCardSlot + " production.");
            }
        }
        return tempMap;
    }

    /**
     * return the faith marker position of the player
     *
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
