package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.PlayableCardReaction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotBuilder;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddDepotEffect extends Effect{


    /**
     * This is the constructor of the class. It needs a ResourceType that will be used to determine what Resource can be stored in this depot.
     * @param res that defines what can be stored.
     */
    @JsonCreator
    public AddDepotEffect(@JsonProperty("resource") ResourceType res) {
        this.res = res;
    }

    /**
     * This attribute saves the type of resource that can be stored in the depot.
     */
    private ResourceType res;

    /**
     * This method is activated by a LeaderCard, it adds a depot that contains up to two resources.
     * @param p the player that is getting a new depot.
     */
    @Override
    public void use(CardReaction p) {
        ((PlayableCardReaction) p).addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildFromType(res, 1)));
    }
}
