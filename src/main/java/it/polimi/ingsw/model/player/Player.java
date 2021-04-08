package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.IllegalMovesException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.PopeTile;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.state.Context;
import it.polimi.ingsw.model.player.state.NotHisTurnState;
import it.polimi.ingsw.model.player.state.State;
import it.polimi.ingsw.model.player.state.StateChanger;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.List;
import java.util.Optional;

/**
 * This class identify the Player
 */
public class Player implements Context, StateChanger, PlayerModifier {
    /**
     * represent the player state and contains the implementation of the method of PlayerModifier
     */
    private State playerState;

    /**
     * this is the personal board associated to the player
     */
    private final PersonalBoard personalBoard;

    /**
     * This attribute is the nickname of the Player, it is unique for each player in the match
     */
    private final String nickname;

    /**
     * This attribute is the list of all the Discounts that has the player, that he can use when use the MarketTray
     */
    private List<Optional<Resource>> marketDiscount;

    //TODO implementare un modo di salvare le marble conversion;

    /**
     * This method is the class constructor
     * @param nickname that identify the player
     */
    public Player(String nickname){
        this.nickname = nickname;
        this.personalBoard = new PersonalBoard();
        this.playerState = new NotHisTurnState(this);
    }

    /**
     * This method set the nickname of the Player
     * @return the nickname of the player
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * receive a state and set it as current state
     *
     * @param newState the new state of the context
     */
    @Override
    public void setState(State newState) {
        this.playerState = newState;
    }

    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     *
     * @param leaderCard the leader card to assign to the hand of the player
     */
    @Override
    public void addLeader(LeaderCard leaderCard) {

    }

    /**
     * This method activates the special ability of the LeaderCard
     *
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) {

    }

    /**
     * This method removes a LeaderCard from the player
     *
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) {

    }

    /**
     * This method adds a Production to the list of available productions
     *
     * @param newProd the new production
     */
    @Override
    public void addProduction(Production newProd) {

    }

    /**
     * This method select the productions that will be activated by the player
     *
     * @param prod the production to set as selected
     */
    @Override
    public void selectProduction(ProductionID prod) {

    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     *
     * @param prod this identify the production that is required to be activated
     */
    @Override
    public void activateProduction(ProductionID prod) {

    }

    /**
     * This method adds an extra Depot in the Warehouse
     *
     * @param depot new depot to be added to Warehouse depots
     */
    @Override
    public void addDepot(Depot depot) {

    }

    /**
     * This method gives a discount to the player when buying DevCards
     *
     * @param discount the new discount
     */
    @Override
    public void addDiscount(Resource discount) {

    }

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     *
     * @param newDevCard the dev card received that need to be stored in the personal board
     */
    @Override
    public void receiveDevCard(DevCard newDevCard) {

    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param resource the resource
     */
    @Override
    public void obtainResource(Resource resource) {

    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param marble the resource in form of marble
     */
    @Override
    public void obtainResource(Marble marble) {

    }

    /**
     * This method allows the player to move Resources between Depots
     *
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot the resource to move
     */
    @Override
    public void moveResourceDepot(DepotSlot from, DepotSlot to, Resource loot) {

    }

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     *
     * @param popeTile the tile to check if it need to be flipped
     */
    @Override
    public void flipPopeTile(PopeTile popeTile) {

    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     *
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveFaithMarker(int amount) {

    }

    /**
     * This method moves the Lorenzo's marker
     *
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveLorenzo(int amount) {

    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     *
     * @param resource resource to set as default as white marble conversion
     */
    @Override
    public void selectWhiteConversion(Resource resource) {

    }

    /**
     * This method allow adding a marble conversion to the player
     *
     * @param fromWhite the resource type to transform white marbles
     */
    @Override
    public void addMarbleConversion(ResourceType fromWhite) {

    }

    /**
     * player start the turn
     */
    @Override
    public void startTurn() throws IllegalMovesException {
        playerState.startTurn();
    }

    /**
     * player has done a leader action
     */
    @Override
    public void mainActionDone() throws IllegalMovesException {
        playerState.mainActionDone();
    }

    /**
     * player end his turn
     */
    @Override
    public void endTurn() throws IllegalMovesException {
        playerState.endTurn();
    }

    /**
     * only for test, return the current player state
     */
    public State getPlayerState(){
        return playerState;
    }
}
