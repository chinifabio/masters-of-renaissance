package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteWhiteMarbleEffect;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.PlayableCardReaction;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class WhiteMarbleEffect extends Effect{

    /**
     * This is the constructor of the class. It needs a ResourceType which is the one that white marbles will be converted to during the Market phase.
     * @param conv that defines the resource the player will get from white marbles.
     */
    @JsonCreator
    public WhiteMarbleEffect(@JsonProperty("marble") Marble conv) {
        this.conv = conv;
    }

    /**
     * This attribute saves the ResourceType of the white marbles.
     */
    private final Marble conv;

    /**
     * This method is activated by a LeaderCard, it gives the opportunity to convert every White Marble into a specific resource.
     * @param p the player that is getting the conversion bonus.
     */
    @Override
    public void use(CardReaction p) {
        ((PlayableCardReaction) p).addMarbleConversion(conv);
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteEffect liteVersion() {
        return new LiteWhiteMarbleEffect(this.conv.liteVersion());
    }
}
