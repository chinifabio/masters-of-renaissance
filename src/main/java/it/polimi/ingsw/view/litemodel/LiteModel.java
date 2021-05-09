package it.polimi.ingsw.view.litemodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteModel {

    private final Map<String, LitePersonalBoard> players = new HashMap<>();
    private String me;

    private List<String> tray;
    private List<String> devSetup;

    public synchronized void setLeader(String nickname, List<String> cards) {
        this.players.get(nickname).setLeader(cards);
    }

    public synchronized void setMarbleMarket(List<String> tray) {
        this.tray = tray;
    }

    public synchronized void setDevSetup(List<String> devSetup) {
        this.devSetup = devSetup;
    }

    public synchronized List<String> getLeader(String nickname) {
        return this.players.get(nickname).getLeaderCards();
    }

    public synchronized List<String> getTray() {
        return tray;
    }

    public synchronized List<String> getDevSetup() {
        return devSetup;
    }

    public synchronized void createMePlayer(String nickname) {
        this.me = nickname;
        this.players.put(nickname, new LitePersonalBoard());
    }

    private LiteWarehouse warehouse;

    private LiteFaithTrack track;

    public LiteWarehouse getWarehouse(){
        return this.warehouse;
    }

    public void setWarehouse(LiteWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LiteFaithTrack getTrack() {
        return track;
    }

    public void setTrack(LiteFaithTrack track) {
        this.track = track;
    }
}
