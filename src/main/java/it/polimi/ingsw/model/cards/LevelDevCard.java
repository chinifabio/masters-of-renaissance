package it.polimi.ingsw.model.cards;

/**
 * This enumeration is the Level of the DevCard
 */
public enum LevelDevCard {
    LEVEL1 (1), LEVEL2 (2), LEVEL3 (3), NOLEVEL(-1);

    /**
     * This attribute is the value of the card's level
     */
    private final int levelCard;

    /**
     * This is the constructor of the class
     * @param levelCard is the level of the card
     */
    LevelDevCard(int levelCard){
        this.levelCard = levelCard;
    }

    /**
     * @return the level of the card
     */
    public int getLevelCard() {
        return this.levelCard;
    }
}