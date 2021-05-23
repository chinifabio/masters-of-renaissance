package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

/**
 * This enumeration identifies the location of the objects to which the production is assigned
 */
public enum ProductionID {
    BASIC, LEFT, CENTER, RIGHT, LEADER1, LEADER2;

    public static ProductionID[] special() {
        return new ProductionID[]{LEADER1, LEADER2};
    }
}
