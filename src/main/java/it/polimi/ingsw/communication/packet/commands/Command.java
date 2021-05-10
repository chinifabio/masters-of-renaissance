package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This interface is used to contain a command to be execute on a playerAction interface
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "BuyDevCard", value = BuyDevCardCommand.class),
        @JsonSubTypes.Type(name = "DiscardLeader", value = DiscardLeaderCommand.class),
        @JsonSubTypes.Type(name = "PaintMarble", value = PaintMarbleCommand.class),
        @JsonSubTypes.Type(name = "UseTray", value = UseMarketTrayCommand.class),
})
public abstract class Command {
    /**
     * The command to execute on a player action interface
     * @param player the player on which execute the command
     */
    public abstract Packet execute(PlayerAction player);

    /**
     * Return a json representation of the command
     * @return json serialized string of the command
     */
    public String jsonfy(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
