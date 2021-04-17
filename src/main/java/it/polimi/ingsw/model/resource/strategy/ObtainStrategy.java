package it.polimi.ingsw.model.resource.strategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * strategy for obtaining resource
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "DoNothing", value = DoNothingBehavior.class),
        @JsonSubTypes.Type(name = "GivePoint", value = GiveFaithPointBehavior.class),
        @JsonSubTypes.Type(name = "LaunchExc", value = LaunchExceptionBehavior.class)
})
public interface ObtainStrategy {
    /**
     * method to invoke when a resource is obtained
     * @param player the player who obtain the resource
     * @throws UnobtainableResourceException launched if the resource is not obtainable
     */
    void obtain(PlayerReactEffect player) throws UnobtainableResourceException, WrongPointsException, IllegalMovesException;
}
