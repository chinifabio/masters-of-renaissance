package it.polimi.ingsw.model.match.match;

public class MultiplayerMatch extends Match{
    /**
     * build a multiplayer match.
     * A multiplayer match has a game size of 4 players
     * and require at least 2 player to be started
     */
    public MultiplayerMatch() {
        super(4, 2);
    }
}
