package it.polimi.ingsw.litemodel;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteModel {

    private final Map<String, LitePersonalBoard> players = new HashMap<>();

    private String me;

    private List<String> tray;
    private List<String> devSetup;
    private int numberOfPlayer = 0;

    public synchronized void createPlayer(String nickname) {
        this.players.put(nickname, new LitePersonalBoard());
        this.numberOfPlayer ++;
    }

// --------------- SETTER METHODS ------------------

    public synchronized void setMyNickname(String nickname) {
        this.me = nickname;
    }

    public synchronized void setLeader(String nickname, List<String> cards) {
        this.players.get(nickname).setLeader(cards);
    }

    public synchronized void setMarbleMarket(List<String> tray) {
        this.tray = tray;
    }

    public synchronized void setDevSetup(List<String> devSetup) {
        this.devSetup = devSetup;
    }


// ------------------- GETTER METHODS ------------------

    public synchronized List<String> getLeader(String nickname) {
        return this.players.get(nickname).getLeaderCards();
    }

    public synchronized List<String> getTray() {
        return tray;
    }

    public synchronized List<String> getDevSetup() {
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

    public synchronized void movePlayer(String nickname, int amount) {
        this.players.get(nickname).movePlayer(amount);
    }

    public synchronized void flipPopeTile(String nickname, String popeTile) {
        this.players.get(nickname).flipPopeTile(popeTile);
    }

    public void setDepot(String nickname, DepotSlot slot, LiteDepot depot) {
        this.players.get(nickname).setDepot(slot, depot);
    }

    public int playersInGame() {
        return numberOfPlayer;
    }
}
