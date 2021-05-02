package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.RequisiteType;
import it.polimi.ingsw.model.resource.Resource;

import java.util.*;

/**
 * This class identify the Player's PersonalBoard
 */
public class PersonalBoard {

    /**
     * This attribute is the Deck of the LeaderCards owned by the Player
     */
    private final Deck<LeaderCard> leaderDeck;

    /**
     * This attribute is the Deck of the DevCards owned by the Player and their position in the PersonalBoard
     */
    private final Map<DevCardSlot, Deck<DevCard>> devDeck;

    /**
     * This attribute maps the ProductionID to the DevCardSlot
     */
    private final Map<DevCardSlot, ProductionID> productionSlotMap;

    /**
     * This attribute is the Warehouse contained into the PersonalBoard
     */
    private final Warehouse warehouse;

    /**
     * This attribute is the FaithTrack of the PersonalBoard
     */
    private final FaithTrack faithTrack;

    /**
     * This attribute is the Player that own this PersonalBoard
     */
    private final Player player;


    /**
     * This method is the constructor of the class
     * @param player the player who own this personal board
     */
    public PersonalBoard(Player player) throws IllegalTypeInProduction {
        this.leaderDeck = new Deck<>();

        this.devDeck = new EnumMap<>(DevCardSlot.class);
        devDeck.put(DevCardSlot.LEFT, new Deck<>());
        devDeck.put(DevCardSlot.CENTER, new Deck<>());
        devDeck.put(DevCardSlot.RIGHT, new Deck<>());

        this.productionSlotMap = new EnumMap<>(DevCardSlot.class);
        productionSlotMap.put(DevCardSlot.LEFT, ProductionID.LEFT);
        productionSlotMap.put(DevCardSlot.CENTER, ProductionID.CENTER);
        productionSlotMap.put(DevCardSlot.RIGHT, ProductionID.RIGHT);

        this.warehouse = new Warehouse();
        this.faithTrack = new FaithTrack();
        this.player = player;
    }

    /**
     * This method adds the DevCard bought by the Player into the DevCardSlot
     * @param slot is the slot where the DevCard is inserted
     * @param card is the DevCard bought by the Player
     * @param pm is the Player that do this action
     * @return true if the card is correctly added
     */
    public boolean addDevCard(DevCardSlot slot, DevCard card, PlayerToMatch pm) {
        int sum = 0;
        for (DevCardSlot key : DevCardSlot.values()) sum += devDeck.get(key).getNumberOfCards();
        if (sum >= 7) {
            pm.startEndGameLogic();
            return false;
        }
        if (checkDevCard(slot, card)) {
            try {
                this.devDeck.get(slot).insertCard(card);
                return true;
            } catch (AlreadyInDeckException e) {
                e.printStackTrace();    //should not be possible to enter here
            }
        }
        return false;
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
        if (temp == null) temp = new Deck<>();

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
        for (DevCardSlot devCardSlot : DevCardSlot.values()) {
            try {
                tempMap.put(devCardSlot, this.devDeck.get(devCardSlot).peekFirstCard());
            } catch (EmptyDeckException e) {
                e.printStackTrace();
            }
        }
        return tempMap;
    }


    /**
     * This method add the LeaderCard in the Player's PersonalBoard
     * @param card is the LeaderCard that the Player has chosen
     */
    public void addLeaderCard(LeaderCard card) throws AlreadyInDeckException {
        this.leaderDeck.insertCard(card);
    }

    /**
     * This method activates the SpecialAbility of the LeaderCard after checking if the Player has the requisite to
     * activate the LeaderCard.
     * @param selected is the LeaderCard to activate
     * @return true if the LeaderCard has been activated
     * @throws MissingCardException if the card to activate isn't in the Deck of LeaderCard
     * @throws EmptyDeckException if the Deck is empty
     * @throws LootTypeException if a ResourceRequisite is compared to a CardRequisite
     */
    public boolean activateLeaderCard(String selected) throws MissingCardException, EmptyDeckException, LootTypeException, WrongDepotException {    //TODO implementation - effect
        LeaderCard card = this.leaderDeck.peekCard(selected);

        for (Requisite req : card.getRequirements()) {
            if (req.getRequisiteType() == RequisiteType.RESOURCE) {
                for (Resource resource : warehouse.getTotalResources()) {
                    if (resource.equalsType(req) && resource.amount() < req.getAmount()) return false;
                }
            } else if (req.getRequisiteType() == RequisiteType.CARD) {
                int pbAmountCard = 0;
                for (Map.Entry<DevCardSlot, Deck<DevCard>> entry : devDeck.entrySet()) {
                    for (DevCard devCard : entry.getValue().getCards()) {
                        if (devCard.getColor().equals(req.getColor()) && devCard.getLevel().equals(req.getLevel())) {
                            pbAmountCard++;
                        }
                    }
                }
                if (pbAmountCard < req.getAmount()) return false;
            } else if (req.getRequisiteType() == RequisiteType.COLOR){
                int pbAmountColor = 0;
                for (Map.Entry<DevCardSlot, Deck<DevCard>> entry : devDeck.entrySet()) {
                    for (DevCard devCard : entry.getValue().getCards()) {
                        if (devCard.getColor().equals(req.getColor())) {
                            pbAmountColor++;
                        }
                    }
                }
                if (pbAmountColor < req.getAmount()) return false;
            }
        }
        card.activate();
        card.useEffect(this.player);
        return true;
    }


    /**
     * This method remove the LeaderCard from Player's PersonalBoard
     * @param card is the card to be removed
     * @throws EmptyDeckException if the Deck of the LeaderCard is empty
     * @throws MissingCardException if the LeaderCard to discard isn't in the Deck
     */
    public void discardLeaderCard(String card) throws EmptyDeckException, MissingCardException {
        this.leaderDeck.discard(card);
    }

    /**
     * return all the leader card of the player packed in a deck
     * @return deck of leader card
     */
    public Deck<LeaderCard> viewLeaderCard() {
        return this.leaderDeck;
    }

    /**
     * This method add a new Production into the list of availableProductions
     * @param prod is the Production to add
     * @param slotDestination the destination of the production
     */
    public void addProduction(Production prod, DevCardSlot slotDestination){
        this.warehouse.addProduction(this.productionSlotMap.get(slotDestination), prod);
    }

    /**
     * This method add a new Production into the list of availableProductions
     * @param prod is the Production to add
     * @throws ExtraProductionException if the Player can't obtain other productions
     */
    public void addExtraProduction(Production prod) throws ExtraProductionException {
        this.warehouse.addExtraProduction(prod);
    }

    /**
     * return all the available production
     * @return a map containing productions
     */
    public Map<ProductionID, Production> possibleProduction(){
        Map<ProductionID, Production> temp = new EnumMap<>(ProductionID.class);
        temp.putAll(this.warehouse.getProduction());
        return temp;
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return true if the Resources are correctly moved in Production
     * @throws UnknownUnspecifiedException if the Production is Unspecified
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     * @throws WrongDepotException if the Player can't take Resources from that Depot
     */
    public boolean moveInProduction(DepotSlot from, ProductionID dest, Resource loot) throws UnknownUnspecifiedException, NegativeResourcesDepotException, WrongDepotException {
        return this.warehouse.moveInProduction(from, dest, loot);
    }


    /**
     * This method activate the productions selected by the Player
     * @throws UnobtainableResourceException if the Player can't obtain that Resources
     * @throws EndGameException if the Player can't do this action
     * @throws WrongDepotException if the Player can't insert Resources in that Depot
     */
    public void activateProductions() throws UnobtainableResourceException, EndGameException, WrongDepotException {
        this.warehouse.activateProductions();
    }

    /**
     * This method set the normal production of an unknown production
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     * @return the succeed of the operation
     * @throws IllegalNormalProduction if the Production is already a NormalProduction
     */
    public boolean setNormalProduction(ProductionID id, NormalProduction normalProduction) throws IllegalNormalProduction {
        return this.warehouse.setNormalProduction(id, normalProduction);
    }

    /**
     * This method stores the resource in the buffer depot, then it will be the player to move
     * from buffer depot to a legal one
     * @param slot is the DepotSlot where the Resource will be inserted
     * @param resource is the resource obtained
     * @return true if the Resource is correctly inserted
     * @throws WrongDepotException if the Player can't insert the Resources in that Depot
     */
    public boolean insertInDepot(DepotSlot slot, Resource resource) throws WrongDepotException {
        return this.warehouse.insertInDepot(slot, resource);
    }

    /**
     * This method shows the resources inside a Depot
     * @param slot is the Depot where the resources are stored
     * @return the Resources inside the Depot
     */
    public List<Resource> viewDepotResource(DepotSlot slot){
        return this.warehouse.viewResourcesInDepot(slot);
    }

    /**
     * Create a new depot in the warehouse
     * @param depot is the new Depot
     * @throws ExtraDepotsException if the Player can't add more Depots
     */
    public void addDepot(Depot depot) throws ExtraDepotsException {
        warehouse.addDepot(depot);
    }

    /**
     * This method moves resources from a Depot to another one
     * @param from is the Depot where the resources are taken from
     * @param to is the Depot where the resources will be stored
     * @param resource is the resource to move
     * @throws WrongDepotException if the Depot "from" is empty or doesn't have the same type of resources of "resource"
     * @throws NegativeResourcesDepotException if the Depot "from" hasn't enough resources to move
     * @throws UnobtainableResourceException if the Player can't receive that Resource
     */
    public void moveResourceDepot(DepotSlot from, DepotSlot to, Resource resource) throws WrongDepotException, NegativeResourcesDepotException, UnobtainableResourceException {
        warehouse.moveBetweenDepot(from,to, resource);
    }

    /**
     * tells to the faith track to move amount times the player marker.
     * @param amount the amount to move.
     * @param pm is the Player that own the FaithMarker
     * @return true if the move is allowed, else false.
     */
    public boolean moveFaithMarker(int amount, PlayerToMatch pm) {
        try {
            this.faithTrack.movePlayer(amount, pm);
        } catch (EndGameException e) {
            pm.startEndGameLogic();
        }
        return true;
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
     * Discard all the resources in the buffer depot and for all of them give faith point at all other player
     * @param p2m is the Player that will receives the FaithPoints
     */
    public void flushBufferDepot(PlayerToMatch p2m){
        List<Resource> list = this.warehouse.viewResourcesInDepot(DepotSlot.BUFFER);
        int fp = 0;
        for (Resource resource : list) {
            fp += resource.amount();
        }
        p2m.othersPlayersObtainFaithPoint(fp);
        this.warehouse.flushBufferDepot();
    }

    /**
     * ? Non si può usare la funzione sopra ^ ?
     */
    public void flushBufferDevCard(){
        this.warehouse.flushBufferDepot();
    }

    /**
     * This method counts all the victoryPoints that the Player has earned during the game
     * @return the total value of all victoryPoints
     */
    public int getTotalVictoryPoints(){
        int points = 0;
        points = points + warehouse.countPointsWarehouse();
        points = points + faithTrack.countingFaithTrackVictoryPoints();
        points = points + this.getVictoryPointsDevCards();
        points = points + this.getVictoryPointsLeaderCards();
        return points;
    }

    /**
     * This method counts the value of VictoryPoints of the DevCards in the PersonalBoard
     * @return the total value of VictoryPoints of the DevCards
     */
    public int getVictoryPointsDevCards(){
        int points = 0;
        for (Map.Entry<DevCardSlot, Deck<DevCard>> entry : devDeck.entrySet()){
            for (DevCard card : entry.getValue().getCards()){
                points = points + card.getVictoryPoint();
            }
        }
        return points;
    }

    /**
     * This method counts the value of VictoryPoints of the LeaderCards activated by the Player
     * @return the total value of VictoryPoints of the LeaderCards
     */
    public int getVictoryPointsLeaderCards(){
        int points = 0;
        for (LeaderCard card : leaderDeck.getCards()){
            if (card.isActivated()){
                points = points + card.getVictoryPoint();
            }
        }
        return points;
    }

    // only for testing
    public FaithTrack getFT_forTest() {
        return this.faithTrack;
    }

    //only for testing
    public Warehouse getWH_forTest(){
        return this.warehouse;
    }

    // only for testing
    public Map<DepotSlot, Depot> test_getDepots() {
        return this.warehouse.test_getDepot();
    }

    // only for testing
    public Map<ProductionID, Production> test_getProduction() {
        return this.warehouse.test_getProduction();
    }
}
