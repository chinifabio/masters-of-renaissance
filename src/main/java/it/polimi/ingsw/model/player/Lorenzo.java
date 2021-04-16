package it.polimi.ingsw.model.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.exceptions.gameexception.LorenzoMovesException;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.TextColors.*;

public class Lorenzo extends Player{

    /**
     * This is the faith track of the player Lorenzo
     */
    private FaithTrack faithTrack;

    /**
     * The solo action token used at end turn of a single player match
     */
    private final Deck<SoloActionToken> soloToken;

    public static final String lorenzoNickname = CYAN + "Lorenzo il Magnifico" + RESET ;

    /**
     * This method create a Lorenzo instance, which is a modified Player
     *
     * @param matchReference the match reference
     *
     */
    public Lorenzo(PlayerToMatch matchReference) {

        super(Lorenzo.lorenzoNickname, matchReference);
        this.faithTrack = new FaithTrack();
        List<SoloActionToken> init = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
                init = objectMapper.readValue(
                    new File("src/resources/SoloActionTokens.json"),
                    new TypeReference<List<SoloActionToken>>(){});
        }catch (IOException e){
            System.out.println("The file to create the faith track wasn't found");
            //TODO LANCIARE UN'ECCEZIONE AL MODEL
        }
        this.soloToken = new Deck<>(init);
        soloToken.shuffle();
    }

    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     *
     * @param leaderCard the leader card to assign to the hand of the player
     */
    @Override
    public void addLeader(LeaderCard leaderCard) {
        //todo dire al model errore lorenzo
    }

    /**
     * this method check if the player has requisite.
     * If it return true then the warehouse has eliminate the requisites yet
     * If it return false then the player has not the requisite;
     *
     * @param req the requisite
     * @return boolean indicating the succeed of the method
     */
    @Override
    public boolean hasRequisite(List<Requisite> req,LevelDevCard row, ColorDevCard col) throws NoRequisiteException {
        return false;
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
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     *
     * @param popeTile the tile to check if it need to be flipped
     */
    @Override
    public void flipPopeTile(VaticanSpace popeTile) {

    }

    /**
     * starts the turn of the player;
     *
     * @return true if success, false otherwise
     */
    @Override
    public boolean startHisTurn() {
        super.startHisTurn();
        try {
            System.out.println(lorenzoNickname + ": drawing soloActionToken");
            this.soloToken.draw().useEffect(this);
        } catch (EmptyDeckException e) {

            //TODO dire che qualcosa è andato storto
            return false;
        }
        System.out.println(lorenzoNickname + ": " + RED + "FaithPoint" + RESET + " amount -> " + this.faithTrack.getPlayerPosition());
        super.endThisTurn();
        //this.endThisTurn(); // todo rigurdare se farlo fare al client o lasciarlo automatico così
        //this.match.endMyTurn();
        return true;
    }

    /**
     * This method adds a Production to the list of available productions
     *
     * @param newProd the new production
     */
    @Override
    public void addProduction(Production newProd) {
        try {
            throw new LorenzoMovesException();
        } catch (LorenzoMovesException e) {
            // todo dirlo al model
        }
    }

    /**
     * This method adds an extra Depot in the Warehouse
     *
     * @param depot new depot to be added to Warehouse depots
     */
    @Override
    public void addDepot(Depot depot) {
        try {
            throw new LorenzoMovesException();
        } catch (LorenzoMovesException e) {
            // todo dirlo al model
        }
    }

    /**
     * This method gives a discount to the player when buying DevCards
     *
     * @param discount the new discount
     */
    @Override
    public void addDiscount(Resource discount) {
        try {
            throw new LorenzoMovesException();
        } catch (LorenzoMovesException e) {
            // todo dirlo al model
        }
    }

    /**
     * This method allow adding a marble conversion to the player
     *
     * @param conversion the resource type to transform white marbles
     */
    @Override
    public void addMarbleConversion(Marble conversion) {
        try {
            throw new LorenzoMovesException();
        } catch (LorenzoMovesException e) {
            // todo dirlo al model
        }
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param resource the resource
     */
    @Override
    public void obtainResource(Resource resource) {
        try {
            throw new LorenzoMovesException();
        } catch (LorenzoMovesException e) {
            // todo dirlo al model
        }
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param marble the resource in form of marble
     */
    @Override
    public void obtainResource(Marble marble) {
        try {
            throw new LorenzoMovesException();
        } catch (LorenzoMovesException e) {
            // todo dirlo al model
        }
    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     *
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveFaithMarker(int amount) {
        try {
            this.faithTrack.movePlayer(amount);
        } catch (WrongPointsException | IllegalMovesException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shuffle the solo action token
     */
    @Override
    public void shuffleToken(){
        this.soloToken.shuffle();
    }

    /**
     * This method discard two card of the color passed in the dev setup
     *
     * @param color color of the dev card to discard
     */
    @Override
    public void discardDevCard(ColorDevCard color) {
        this.match.discardDevCard(color);
    }
}
