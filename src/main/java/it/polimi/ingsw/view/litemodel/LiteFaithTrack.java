package it.polimi.ingsw.view.litemodel;

public class LiteFaithTrack {

    private int playerPosition = 0;

    public Integer getPlayerPosition() {
        return this.playerPosition;
    }

    public void movePlayer(int amount) {
        this.playerPosition += amount;
    }
}
