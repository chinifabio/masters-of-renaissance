package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.polimi.ingsw.model.cards.effects.DestroyCardsEffect;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.cards.effects.MoveTwoEffect;
import it.polimi.ingsw.model.cards.effects.ShuffleMoveOneEffect;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerReactEffect;
import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * This abstract class generalize the concept of Card, every Card has a cardID and an effect.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Dev", value = DevCard.class),
        @JsonSubTypes.Type(name = "Token", value = SoloActionToken.class),
        @JsonSubTypes.Type(name = "Leader", value = LeaderCard.class)
})
public abstract class Card{

    public Card(){}
    /**
     * This is the constructor of the class. It needs a cardID and an effect.
     * @param cardID of the Card.
     * @param effect of the Card.
     */
    public Card(String cardID, Effect effect) {
        this.cardID = cardID;
        this.effect = effect;
    }

    /**
     * This attribute is the ID of the card. Every card has a different cardID.
     */
    private String cardID;

    /**
     * This attribute is the effect of the card. The effect is highly tied with the card type.
     */
    private Effect effect;

    /**
     * This method returns the cardID of the card.
     * @return the cardID.
     */
    public String getCardID() {
        return this.cardID;
    }

    /**
     * This method is used by subclasses to implement their effect: AddDepot, AddDiscount, AddProduction, DestroyCards, MoveTwo, ShuffleMoveONe, WhiteMarble.
     * @param p
     */
    public void useEffect(PlayerReactEffect p){
        effect.use(p);
    }


    /**
     * This method overloads the one on the Object class. It permits to check if two cards are equals, comparing their cardIDs.
     * @param card to confront with the first.
     * @return true if cardID are equals, false if not.
     */
    public boolean equals(Card card) {
        return this.cardID.equals(card.cardID);
    }
}
