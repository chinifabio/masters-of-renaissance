package it.polimi.ingsw.model.cards;

/**
 * This enumeration is the Level of the DevCard
 */
public enum LevelDevCard {
    LEVEL1 (1, 2), LEVEL2 (2, 1), LEVEL3 (3, 0), NOLEVEL(-1, -1);

    /**
     * This attribute is the value of the card's level
     */
    private final int levelCard;

    /**
     * This attribute is the row in the cards' grid
     */
    private final int devSetupIndex;

    /**
     * This is the constructor of the class
     * @param levelCard is the level of the card
     * @param index is the index in the card's grid
     */
    LevelDevCard(int levelCard, int index){
        this.levelCard = levelCard;
        this.devSetupIndex = index;
    }

    /**
     * @return the level of the card
     */
    public int getLevelCard() {
        return this.levelCard;
    }

    /**
     * @return is the index in the cards' grid
     */
    //Todo remove (?)
    public int getDevSetupIndex() {
        return devSetupIndex;
    }
}