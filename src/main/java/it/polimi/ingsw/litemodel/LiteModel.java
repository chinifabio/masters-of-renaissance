package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteFaithTrack;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarketTray;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.util.Tuple;

import java.util.*;

public class LiteModel {

    private final Map<String, LitePersonalBoard> players = new HashMap<>();

    private String me;

    private LiteMarketTray tray;
    private LiteDevSetup devSetup;

    private LiteSoloActionToken soloToken;

    @JsonCreator
    public LiteModel(@JsonProperty("players") List<Tuple<String, LitePersonalBoard>> list) {
        list.forEach(x->players.put(x.a, x.b));
    }

    public LiteModel(){}

    public synchronized void createPlayer(String nickname) {
        this.players.put(nickname, new LitePersonalBoard());
    }

// --------------- SETTER METHODS ------------------

    public synchronized void setMyNickname(String nickname) {
        this.me = nickname;
    }

    public synchronized void setLeader(String nickname, List<LiteLeaderCard> cards) {
        this.players.get(nickname).setLeader(cards);
    }

    public void setDevelop(String nickname, List<LiteDevCard> deck) {
        this.players.get(nickname).setDevelop(deck);
    }

    public List<LiteDevCard> getDevelop(String nickname){
        return this.players.get(nickname).getDevelop();
    }

    public synchronized void setDevSetup(LiteDevSetup devSetup) {
        this.devSetup = devSetup;
    }

    public synchronized void movePlayer(String nickname, int amount) {
        this.players.get(nickname).movePlayer(amount);
    }

    public synchronized void updateFaithTrack(String nickname, LiteFaithTrack track) {
        this.players.get(nickname).updateFaithTrack(track);
    }

    public synchronized void setMarketTray(LiteMarketTray lite) {
        this.tray = lite;
    }

    public synchronized void setProduction(String nickname, LiteProduction prod, ProductionID id) {
        this.players.get(nickname).setProduction(id, prod);
    }

    public synchronized void setDepot(String nickname, DepotSlot slot, LiteDepot depot) {
        this.players.get(nickname).setDepot(slot, depot);
    }

    public synchronized void setSoloToken(LiteSoloActionToken token) {
            this.soloToken = token;
    }


    public synchronized void setDiscounts(String nickname, List<LiteResource> discounts) {
        this.players.get(nickname).setDiscounts(discounts);
    }

    public synchronized void setConversions(String nickname, List<MarbleColor> collect) {
        this.players.get(nickname).setConversions(collect);
    }

    public void setPlayerState(String nickname, LiteState state) {
        players.get(nickname).setState(state);
    }

// ------------------- GETTER METHODS ------------------

    public synchronized List<LiteLeaderCard> getLeader(String nickname) {
        return this.players.get(nickname).getLeaderCards();
    }

    public synchronized LiteMarketTray getTray() {
        return tray;
    }

    public synchronized LiteDevSetup getDevSetup() {
        return devSetup;
    }

    public synchronized LiteDepot getDepot(String nickname, DepotSlot slot) {
        return this.players.get(nickname).getDepot(slot);
    }

    public synchronized Map<String, Integer> getPlayerPosition() {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, LitePersonalBoard> entry : this.players.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getPlayerPosition());
        }
        return result;
    }

    public synchronized Map<String, Map<String, Boolean>> getPopeTilesPlayer(){
        Map <String, Map<String, Boolean>> result = new HashMap<>();
        for (Map.Entry<String, LitePersonalBoard> entry : this.players.entrySet()){
            result.put(entry.getKey(), entry.getValue().getPopeTiles());
        }
        return result;
    }

    public synchronized void flipPopeTile(String nickname, String popeTile) {
        this.players.get(nickname).flipPopeTile(popeTile);
    }

    public synchronized Map<ProductionID, LiteProduction> getAllProductions(String nickname){
        return this.players.get(nickname).getWarehouse().getAllProductions();
    }

    public synchronized int playersInGame() {
        return this.players.values().size();
    }

    public synchronized LiteSoloActionToken getSoloToken() {
        return this.soloToken;
    }

    public synchronized String getMe() {
        return this.me;
    }

    public LiteState getPlayerState(String nickname) {
        return this.players.get(nickname).getState();
    }

    /**
     * replace all the model data with the given one
     * @param model
     */
    public void replaceModel(LiteModel model) {
        //this.playerState = model.getPlayerState();

        model.players.forEach(this.players::put);

        this.tray = model.getTray();
        this.devSetup = model.getDevSetup();
    }

    @JsonGetter("players")
    public List<Tuple<String, LitePersonalBoard>> getValues() {
        List<Tuple<String, LitePersonalBoard>> res = new ArrayList<>();

        this.players.forEach((x, y)->res.add(new Tuple<>(x, y)));

        return res;
    }

    public List<LiteResource> getDiscounts(String me) {
        return this.players.get(me).getDiscounts();
    }

    public List<MarbleColor> getConversion(String me){
        return this.players.get(me).getConversions();
    }

    public Map<String, LitePersonalBoard> getPlayers(){
        return this.players;
    }
}
