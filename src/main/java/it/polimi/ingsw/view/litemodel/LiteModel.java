package it.polimi.ingsw.view.litemodel;

public class LiteModel {

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
