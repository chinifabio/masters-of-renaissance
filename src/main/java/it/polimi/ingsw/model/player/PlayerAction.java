package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.WrongDepotException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

import java.util.List;
import java.util.Map;

public interface PlayerAction {

    /**
     * This method return a representation of the tray as a list of marble
     * @return the tray representation as list
     */
    List<Marble> viewMarketTray();

    /**
     * Use the market tray
     * @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return the result of the operation
     */
    boolean useMarketTray(RowCol rc, int index);

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param resource resource to set as default as white marble conversion
     */

    void selectWhiteConversion(Resource resource);
    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     * @return the list representation of the dev setup
     */
    List<DevCard> viewDevSetup();

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     * @param col the column of the card required
     * @param row the row of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     */
    boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination);

    /**
     * return a list of all available production for the player
     * @return the list of production
     */
    List<Production> possibleProduction();

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return  the result of the operation
     */
    boolean activateProductions();

    /**
     * This method select the productions that will be activated by the player
     * @param prod the production to set as selected
     */
    void selectProduction(ProductionID prod);

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     */
    void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws WrongDepotException, NegativeResourcesDepotException;

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    void activateLeaderCard(String leaderId);

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    void discardLeader(String leaderId);

    /**
     * Return a deck of leader card that a player has
     * @return deck of leader card
     */
    Deck<LeaderCard> viewLeader();

    /**
     * return a list of all available player's resource. It doesn't mattare the depot they are stored
     * @return a list of resource
     */
    List<Resource> viewResource();

    /**
     * return a map of the top develop card placed in the player board decks
     * @return a map of devCardSlot - DevCard
     */
    Map<DevCardSlot, DevCard> viewDevCards();

    /**
     * return the faith marker position of the player
     * @return faith marker position of the player
     */
    int viewFaithMarkerPosition();

    /**
     * the player ends its turn
     * @return true if success, false otherwise
     */
    boolean endThisTurn();

    /**
     * this method tell if the player can so stuff
     * @return true if it can, false otherwise
     */
    boolean canDoStuff();
}
