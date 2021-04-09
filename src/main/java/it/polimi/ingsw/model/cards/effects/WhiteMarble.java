package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class WhiteMarble implements Effect{

    /**
     * This is the constructor of the method. It needs a ResourceType which is the one that white marbles will be converted to during the Market phase.
     * @param conv that defines the resource the player will get from white marbles.
     */
    public WhiteMarble(ResourceType conv) {
        this.conv = conv;
    }

    /**
     * This attribute saves the ResourceType of the white marbles.
     */
    private ResourceType conv;

    /**
     * This method is activated by a LeaderCard, it gives the opportunity to convert every White Marble into a specific resource.
     * @param p the player that is getting the conversion bonus.
     */
    @Override
    public void use(PlayerModifier p) {
        p.addMarbleConversion(conv); // manca da aggiungere il conv
    }
}
