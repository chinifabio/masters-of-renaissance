package it.polimi.ingsw.model.cards;

public class SoloActionToken extends Card{

    /**
     * This is the constructor of the class, it needs the cardID because the class extends Card, which requires a cardID.
     * @param cardID for the upper class.
     */
    public SoloActionToken(String cardID) {
        super(cardID);
    }

    /**
     * This method implements the required method from the abstract Card class. It permits to activate different effects: MoveTwo, ShuffleMoveOne, DestroyCards
     */
    @Override
    public void useEffect() {

    }
}
