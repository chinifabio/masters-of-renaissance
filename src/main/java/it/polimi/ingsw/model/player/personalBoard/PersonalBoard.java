package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.requisite.ResourceRequisite;

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
    private List<Production> availableProductions;

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
     * This method is the constructor of the class
     */
    public PersonalBoard personalBoard;

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
    public void activateLeaderCard(LeaderCard card){}

    /**
     * This method remove the LeaderCard from Player's PersonalBoard
     * @param card is the card to be removed
     */
    public void discardLeaderCard(LeaderCard card){}

    /**
     * This method convert the MarketMarble into their respective Resources
     * @param func is the function that do the conversion
     */
    public void MarbleConversion(Function<MarbleColor, ResourceRequisite> func){}

    /**
     * This method add a new Production into the list of availableProductions
     * @param prod is the Production to add
     */
    public void addProduction(Production prod){}

    /**
     * This method select the production that the Player wants to activate
     * @param productionID is the identifier of the Production that will be activated
     */
    public void selectProduction(ProductionID productionID){}

    /**
     * This method activate the productions selected by the Player
     */
    public void activateProductions(){}

}
