package it.polimi.ingsw.litemodel;

import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteFaithTrack;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarketTray;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.litemodel.liteplayer.PendingStart;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteModel {

    private final Map<String, LitePersonalBoard> players = new HashMap<>();

    private String me;

    private LiteMarketTray tray;
    private LiteDevSetup devSetup;
    private int numberOfPlayer = 0;
    private List<LiteSoloActionToken> soloToken;
    private LiteState playerState = new PendingStart();

    public synchronized void createPlayer(String nickname) {
        this.players.put(nickname, new LitePersonalBoard());
        this.numberOfPlayer ++;
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

    public synchronized void setSoloToken(List<LiteSoloActionToken> deck) {
        this.soloToken = deck;
    }


    public synchronized void setDiscounts(String nickname, List<LiteResource> discounts) {
        this.players.get(nickname).setDiscounts(discounts);
    }

    public synchronized void setConversions(String nickname, List<MarbleColor> collect) {
        this.players.get(nickname).setConversions(collect);
    }

    public void setPlayerState(LiteState state) {
        this.playerState = state;
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

    public synchronized int playersInGame() {
        return numberOfPlayer;
    }

    public synchronized List<LiteSoloActionToken> getSoloToken() {
        return new ArrayList<>(this.soloToken);
    }

    public synchronized String getMe() {
        return this.me;
    }

    public LiteState getState() {
        return this.playerState;
    }
}
