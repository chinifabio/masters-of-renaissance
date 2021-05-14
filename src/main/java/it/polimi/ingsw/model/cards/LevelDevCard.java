package it.polimi.ingsw.model.cards;

/**
 * This enumeration is the Level of the DevCard
 */
public enum LevelDevCard {
    LEVEL1 (1, 2), LEVEL2 (2, 1), LEVEL3 (3, 0), NOLEVEL(-1, -1);

    private final int levelCard;

    private final int devSetupIndex;

    LevelDevCard(int levelCard, int index){
        this.levelCard = levelCard;
        this.devSetupIndex = index;
    }

    public int getLevelCard() {
        return this.levelCard;
    }

    public int getDevSetupIndex() {
        return devSetupIndex;
    }
}