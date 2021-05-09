package it.polimi.ingsw.view.litemodel;

import java.util.List;

public class LitePersonalBoard {

    /**
     * This attribute is the representation of the FaithTrack
     */
    private LiteFaithTrack track;
    private List<String> leaderCards;

    public void setLeader(List<String> cards) {
        // todo now use strings, then we will build the associated card in lite model
        this.leaderCards = cards;
    }

    public List<String> getLeaderCards() {
        return leaderCards;
    }

    /**
     * This method update the FaithTrack with the last changes
     * @param track is the new FaithTrack
     */
    public void setTrack(LiteFaithTrack track){
        this.track = track;
    }

    /**
     * This method return the new status of the FaithTrack
     * @return (LiteFaithTrack) faithTrack
     */
    public LiteFaithTrack getTrack(){
        return this.track;
    }
}
