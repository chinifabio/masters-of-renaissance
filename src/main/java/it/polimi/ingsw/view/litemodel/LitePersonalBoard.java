package it.polimi.ingsw.view.litemodel;

import java.util.List;

public class LitePersonalBoard {

    private LiteFaithTrack track;
    private List<String> leaderCards;

    public void setLeader(List<String> cards) {
        // todo now use strings, then we will build the associated card in lite model
        this.leaderCards = cards;
    }

    public List<String> getLeaderCards() {
        return leaderCards;
    }
}
