package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteFaithTrack;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarketTray;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

import java.util.*;

/**
 * This class contains every useful information that the client needs to have to play
 */
public class LiteModel {

    /**
     * This attribute contains every player of the match and the respective PersonalBoard
     */
    private final HashMap<String, LitePersonalBoard> players = new HashMap<>();

    /**
     * This attribute contains the name of the player
     */
    @JsonIgnore
    private String me;

    /**
     * This attribute contains a lite Market Tray
     */
    private LiteMarketTray tray;

    /**
     * This attribute contains the lite DevSetup
     */
    private LiteDevSetup devSetup;

    /**
     * This attribute contains the lite SoloActionToken for single player games
     */
    private LiteSoloActionToken soloToken = new LiteSoloActionToken("null", null);

    /**
     * This attribute contains the actual order of players
     */
    private List<String> playerOrder = new ArrayList<>();

    /**
     * This attribute contains the final scoreboard
     */
    private Scoreboard scoreboard;

    /**
     * This is the constructor of the class
     */
    @JsonIgnore
    private boolean updatedToken;

    @JsonCreator
    public LiteModel(){}

    public synchronized void createPlayer(String nickname) {
        players.put(nickname, new LitePersonalBoard());
    }

    /**
     * This method replace the LiteModel data with the given ones.
     * @param model model of the view
     */
    public void replaceModel(LiteModel model) {
        model.players.forEach(players::put);
        playerOrder = model.playerOrder;

        tray = model.getTray();
        devSetup = model.getDevSetup();

        scoreboard = model.scoreboard;
    }

// --------------- SETTER METHODS ------------------

    /**
     * This method set the client's nickname
     * @param nickname of the player
     */
    public synchronized void setMyNickname(String nickname) {
        this.me = nickname;
    }

    /**
     * This method gives the player a leader card
     * @param nickname of the player
     * @param cards
     */
    public synchronized void setLeader(String nickname, List<LiteLeaderCard> cards) {
        this.players.get(nickname).setLeader(cards);
    }

    /**
     * This method updates the devcard owned by the player
     * @param nickname of the player
     */
    public void setDevelop(String nickname, HashMap<DevCardSlot, List<LiteDevCard>> deck) {
        this.players.get(nickname).setDevelop(deck);
    }

    /**
     * This method updates the devSetup
     * @param LiteDevSetup to set
     */
    public synchronized void setDevSetup(LiteDevSetup devSetup) {
        this.devSetup = devSetup;
    }


    //todo STO METODO È NEL MAIN DI UN PRINTER, SI CANCELLA?
    /**
     * This method moves the player on the faith track
     */
    public synchronized void movePlayer(String nickname, int amount) {
        this.players.get(nickname).movePlayer(amount);
    }

    /**
     * This method updates the whole faith track of a player: position, popetiles
     * @param nickname of the player
     * @param LiteFaithTrack to set
     */
    public synchronized void updateFaithTrack(String nickname, LiteFaithTrack track) {
        this.players.get(nickname).updateFaithTrack(track);
    }

    /**
     * This method updates the Market Tray
     * @param LiteMarketTray to set
     */
    public synchronized void setMarketTray(LiteMarketTray tray) {
        this.tray = tray;
    }

    /**
     * This method updates a production of the player
     * @param nickname of the player
     * @param LiteProduction to set
     * @param ProductionID to set
     */
    public synchronized void setProduction(String nickname, LiteProduction prod, ProductionID id) {
        this.players.get(nickname).setProduction(id, prod);
    }

    /**
     * This method updates a depot of the player
     * @param nickname of the player
     * @param DepotSlot to set
     * @param LiteDepot to set
     */
    public synchronized void setDepot(String nickname, DepotSlot slot, LiteDepot depot) {
        this.players.get(nickname).setDepot(slot, depot);
    }

    /**
     * This method updates the SoloActionToken
     * @param LiteSoloActionToken to set
     */
    public synchronized void setSoloToken(LiteSoloActionToken token) {
        soloToken = token;
    }

    /**
     * This method updates the available discounts
     * @param nickname of the player
     * @param list of LiteResources to set
     */

    public synchronized void setDiscounts(String nickname, List<LiteResource> discounts) {
        this.players.get(nickname).setDiscounts(discounts);
    }

    /**
     * This method updates the available white marble conversion
     * @param nickname of the player
     * @param list of MarbleColors to set
     */
    public synchronized void setConversions(String nickname, List<MarbleColor> collect) {
        this.players.get(nickname).setConversions(collect);
    }

    /**
     * This method updates the order of the player
     * @param playerOrder to set
     */

    public synchronized void setPlayerOrder(List<String> playerOrder){
        this.playerOrder = playerOrder;
    }

    /**
     * This method updates the scoreboard
     * @param scoreboard to set
     */
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

// ------------------- GETTER METHODS ------------------

    /**
     * This method returns the list of LeaderCards of a given player
     * @param nickname of the player
     * @return a list of LiteLeaderCard
     */
    public synchronized List<LiteLeaderCard> getLeader(String nickname) {
        return this.players.get(nickname).getLeaderCards();
    }

    /**
     * This method returns the DevelopmentCards of a given player
     * @param nickname of the player
     * @return a map containing every DevCardSlot and their list of LiteDevCards
     */
    public HashMap<DevCardSlot, List<LiteDevCard>> getDevelop(String nickname){
        return this.players.get(nickname).getDevelop();
    }

    /**
     * This method returns the Market Tray
     * @return the LiteMarketTray
     */
    public synchronized LiteMarketTray getTray() {
        return tray;
    }

    /**
     * This method returns the DevSetup
     * @return the LiteDevSetup
     */
    public synchronized LiteDevSetup getDevSetup() {
        return devSetup;
    }

    /**
     * This method returns a depot of a given player
     * @param nickname of the player
     * @param slot depot to see
     * @return a LiteDepot
     */
    public synchronized LiteDepot getDepot(String nickname, DepotSlot slot) {
        return this.players.get(nickname).getDepot(slot);
    }

    /**
     * This method returns a map that contains the players and their position in the faith track
     * @return a map containing the nickname of the players and their positionion
     */
    public synchronized Map<String, Integer> getPlayerPosition() {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, LitePersonalBoard> entry : this.players.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getPlayerPosition());
        }
        return result;
    }

    /**
     * This method returns the PopeTiles of every player
     * @return a map that contains the nicknames of every player and every PopeTile with their activation parameter
     */
    public synchronized Map<String, HashMap<String, Boolean>> getPopeTilesPlayer(){
        Map <String, HashMap<String, Boolean>> result = new HashMap<>();
        for (Map.Entry<String, LitePersonalBoard> entry : this.players.entrySet()){
            result.put(entry.getKey(), entry.getValue().getPopeTiles());
        }
        return result;
    }

    /**
     * This method returns the player order
     * @return a list of nicknames
     */
    public synchronized List<String> getPlayerOrder(){
        return new ArrayList<>(playerOrder);
    }

    /**
     * This method return the scoreboard
     * @return the scoreboard
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }


    //todo ALTRO METODO USATO SOLO IN STAMPA
    /**
     * This method flips a PopeTile
     */
    public synchronized void flipPopeTile(String nickname, String popeTile) {
        this.players.get(nickname).flipPopeTile(popeTile);
    }

    /**
     * This method returns every production of a given player
     * @param nickname of the player
     * @return a map containing ProductionIDs and LiteProductions
     */
    public synchronized Map<ProductionID, LiteProduction> getAllProductions(String nickname){
        return this.players.get(nickname).getWarehouse().getAllProductions();
    }

    /**
     * This method returns the number of players in the game
     * @return the number of players
     */
    public synchronized int playersInGame() {
        return this.players.values().size();
    }

    /**
     * This method returns the SoloActionToken
     * @return the actual SoloActionToken
     */
    public synchronized LiteSoloActionToken getSoloToken() {
        return this.soloToken;
    }

    /**
     * This method returns the nickname of the player
     * @return the player's nickname
     */
    public synchronized String getMe() {
        return this.me;
    }

    /**
     * This method returns the list available discounts
     * @param nickname of the player
     * @return a list of LiteResource
     */
    public List<LiteResource> getDiscounts(String nickname) {
        return this.players.get(nickname).getDiscounts();
    }

    /**
     * This method returns the list of available conversions
     * @param nickname of the player
     * @return a list of MarbleColor
     */
    public List<MarbleColor> getConversion(String nickname){
        return this.players.get(nickname).getConversions();
    }

    /**
     * This method returns an array of the conversion - used in the GUI
     * @param nickname of the player
     * @return an array of MarbleColor
     */
    public MarbleColor[] getConversionArray(String nickname){
        MarbleColor[] colors = new MarbleColor[0];
        if(convert(nickname,0).isPresent()) {
            colors = new MarbleColor[colors.length+1];
            colors[0] = convert(nickname,0).get();
            if(convert(nickname,1).isPresent()) {
                MarbleColor temp = colors[0];
                colors = new MarbleColor[colors.length+1];
                colors[0] = temp;
                colors[1] = convert(nickname,1).get();
            }
        }
        return colors;
    }

    /**
     * This method returns an Optional of a MarbleColor - used by the method: getConversionArray(String me)
     * It returns null if the list is empty or if its size is 1 and i'm checking the second element
     * else return the given element
     * @param nickname of the player
     * @param x the wanted conversion power
     * @return an Optional MarbleColor
     */
    private Optional<MarbleColor> convert(String nickname, int x){
        List<MarbleColor> list = this.players.get(nickname).getConversions();
        if(list.isEmpty()) return Optional.empty();
        if(list.size()==1 && x==1) return Optional.empty();
        Optional<MarbleColor> temp;
        temp = Optional.ofNullable(list.get(x));
        return temp;
    }

    /**
     * This method returns the players and their PersonalBoard in the game
     * @return A map containing nicknames and personalboards
     */
    public Map<String, LitePersonalBoard> getPlayers(){
        return this.players;
    }
}
