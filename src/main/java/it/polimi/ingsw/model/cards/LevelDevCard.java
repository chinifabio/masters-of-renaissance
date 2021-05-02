package it.polimi.ingsw.model.cards;

/**
 * This enumeration is the Level of the DevCard
 */
public enum LevelDevCard {
    LEVEL1 (1), LEVEL2 (2), LEVEL3 (3);

    private final int levelCard;

    LevelDevCard(int levelCard){
        this.levelCard = levelCard;
    }

    public int getLevelCard() {
        return this.levelCard;
    }
}
