package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

public class LiteModel {

    private final HashMap<String, LitePersonalBoard> players = new HashMap<>();

    @JsonIgnore
    private String me;

    private LiteMarketTray tray;
    private LiteDevSetup devSetup;

    private LiteSoloActionToken soloToken;

    private List<String> playerOrder = new ArrayList<>();

    private Scoreboard scoreboard;

    @JsonCreator
    public LiteModel(){}

    public synchronized void createPlayer(String nickname) {
        this.players.put(nickname, new LitePersonalBoard());
    }

    /**
     * replace all the model data with the given one
     * @param model model of the view
     */
    public void replaceModel(LiteModel model) {
        model.players.forEach(this.players::put);
        playerOrder = model.playerOrder;

        this.tray = model.getTray();
        this.devSetup = model.getDevSetup();

        this.soloToken = model.soloToken;
        this.scoreboard = model.scoreboard;
    }

// --------------- SETTER METHODS ------------------

    public synchronized void setMyNickname(String nickname) {
        this.me = nickname;
    }

    public synchronized void setLeader(String nickname, List<LiteLeaderCard> cards) {
        this.players.get(nickname).setLeader(cards);
    }

    public void setDevelop(String nickname, HashMap<DevCardSlot, List<LiteDevCard>> deck) {
        this.players.get(nickname).setDevelop(deck);
    }

    public HashMap<DevCardSlot, List<LiteDevCard>> getDevelop(String nickname){
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

    public synchronized void setPlayerOrder(List<String> playerOrder){
        this.playerOrder = playerOrder;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
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

    public synchronized Map<String, HashMap<String, Boolean>> getPopeTilesPlayer(){
        Map <String, HashMap<String, Boolean>> result = new HashMap<>();
        for (Map.Entry<String, LitePersonalBoard> entry : this.players.entrySet()){
            result.put(entry.getKey(), entry.getValue().getPopeTiles());
        }
        return result;
    }

    public synchronized List<String> getPlayerOrder(){
        return new ArrayList<>(playerOrder);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
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

    public List<LiteResource> getDiscounts(String me) {
        return this.players.get(me).getDiscounts();
    }

    public List<MarbleColor> getConversion(String me){
        return this.players.get(me).getConversions();
    }

    public MarbleColor[] getConversionArray(String me){
        List<MarbleColor> list = this.players.get(me).getConversions();
        MarbleColor[] colors = new MarbleColor[0];
        if(convert(list, 0).isPresent()) {
            colors = new MarbleColor[colors.length+1];
            colors[0] = convert(list, 0).get();
            if(convert(list, 1).isPresent()) {
                MarbleColor temp = colors[0];
                colors = new MarbleColor[colors.length+1];
                colors[0] = temp;
                colors[1] = convert(list, 0).get();
            }
        }
        return colors;
    }

    private Optional<MarbleColor> convert(List<MarbleColor> list, int x){
        if(list.isEmpty()) return Optional.empty();
        if(list.size()==1 && x==1) return Optional.empty();
        Optional<MarbleColor> temp;
        temp = Optional.ofNullable(list.get(x));
        return temp;
    }

    public Map<String, LitePersonalBoard> getPlayers(){
        return this.players;
    }
}
