package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resource.Resource;

/**
 * This interface includes all the methods that can be called to modify the Player status and attributes
 */
public interface PlayerModifier {
    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     */
    void addLeader();

    /**
     * This method activates the special ability of the LeaderCard
     */
    void activateLeaderCard();

    /**
     * This method adds a Production to the list of available productions
     */
    void addProduction();

    /**
     * This method select the productions that will be activated by the player
     */
    void selectProduction();

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     */
    void activateProduction();

    /**
     * This method adds an extra Depot in the Warehouse
     */
    void addDepot();

    /**
     * This method gives a discount to the player when buying DevCards
     */
    void addDiscount();

    /**
     * This method removes a LeaderCard from the player
     */
    void discardLeader();

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     */
    void buyDevCard();

    /**
     * This method insert the Resources obtained from the Market to the Depots
     */
    void obtainResource(Resource resource);

    /**
     * This method allows the player to move Resources between Depots
     */
    void moveResourceDepot();

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     */
    void flipPopeTile();

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many faith point the player obtains
     */
    void moveFaithMarker(int amount);

    /**
     * This method moves the Lorenzo's marker
     */
    void moveLorenzo();

    /**
     * This method converts the Marbles from the Market to Resources
     */
    Resource WhiteMarbleConversion();

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     */
    void selectWhiteConversion();
}
