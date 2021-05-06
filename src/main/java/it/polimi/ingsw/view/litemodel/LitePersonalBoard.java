package it.polimi.ingsw.view.litemodel;

public class LitePersonalBoard {

    /**
     * This attribute is the representation of the FaithTrack
     */
    private LiteFaithTrack track;

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
