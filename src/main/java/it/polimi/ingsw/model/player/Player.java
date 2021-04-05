package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resource.Resource;

import java.util.List;
import java.util.Optional;

/**
 * This class identify the Player
 */
public class Player implements PlayerModifier{
    /**
     * This attribute is the nickname of the Player, it is unique for each player in the match
     */
    private final String nickname;

    /**
     * This attribute is the list of all the Discounts that has the player, that he can use when use the MarketTray
     */
    private List<Optional<Resource>> marketDiscount;

    /**
     * This method is the class constructor
     * @param nickname that identify the player
     */
    public Player(String nickname){
        this.nickname = nickname;

    }

    /**
     * This method set the nickname of the Player
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * This method returns the LeaderCards that owns the player
     */
    public void getLeaderCard(){}

    /**
     * This method returns the PersonalBoard of the player
     */
    public void getPersonalBoard(){
    }

    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     */
    @Override
    public void addLeader() {

    }

    /**
     * This method activates the special ability of the LeaderCard
     */
    @Override
    public void activateLeaderCard() {

    }

    /**
     * This method adds a Production to the list of available productions
     */
    @Override
    public void addProduction() {

    }

    /**
     * This method select the productions that will be activated by the player
     */
    @Override
    public void selectProduction() {

    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     */
    @Override
    public void activateProduction() {

    }

    /**
     * This method adds an extra Depot in the Warehouse
     */
    @Override
    public void addDepot() {

    }

    /**
     * This method gives a discount to the player when buying DevCards
     */
    @Override
    public void addDiscount() {

    }

    /**
     * This method removes a LeaderCard from the player
     */
    @Override
    public void discardLeader() {

    }

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     */
    @Override
    public void buyDevCard() {

    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     */
    @Override
    public void obtainResource(Resource resource) {

    }

    /**
     * This method allows the player to move Resources between Depots
     */
    @Override
    public void moveResourceDepot() {

    }

    /**
     * This method flips the PopeTile when the Player is (or passed) a PopeSpace
     */
    @Override
    public void flipPopeTile() {

    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many faith points the player obtains
     */
    @Override
    public void moveFaithMarker(int amount) {

    }

    /**
     * This method moves the Lorenzo's marker
     */
    @Override
    public void moveLorenzo() {

    }

    /**
     * This method converts the Marbles from the Market to Resources
     */
    @Override
    public Resource WhiteMarbleConversion() {
        return null;
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     */
    @Override
    public void selectWhiteConversion() {

    }
}
