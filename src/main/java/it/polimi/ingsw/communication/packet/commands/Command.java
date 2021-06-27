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
        @JsonSubTypes.Type(name = "ActivateLeader", value = ActivateLeaderCommand.class),
        @JsonSubTypes.Type(name = "ActivateProduction", value = ActivateProductionCommand.class),
        @JsonSubTypes.Type(name = "BuyDevCard", value = BuyDevCardCommand.class),
        @JsonSubTypes.Type(name = "BuyCard", value = BuyCardCommand.class),
        @JsonSubTypes.Type(name = "Return", value = ReturnCommand.class),
        @JsonSubTypes.Type(name = "ChooseResource", value = ChooseResourceCommand.class),
        @JsonSubTypes.Type(name = "DiscardLeader", value = DiscardLeaderCommand.class),
        @JsonSubTypes.Type(name = "EndTurn", value = EndTurnCommand.class),
        @JsonSubTypes.Type(name = "MoveDepot", value = MoveDepotCommand.class),
        @JsonSubTypes.Type(name = "Production", value = ProductionCommand.class),
        @JsonSubTypes.Type(name = "MoveInProduction", value = MoveInProductionCommand.class),
        @JsonSubTypes.Type(name = "PaintMarble", value = PaintMarbleCommand.class),
        @JsonSubTypes.Type(name = "SetNormalProduction", value = SetNormalProductionCommand.class),
        @JsonSubTypes.Type(name = "UseMarketTray", value = UseMarketTrayCommand.class),
        @JsonSubTypes.Type(name = "ResourceCheatCommand", value = ResourceCheatCommand.class),
        @JsonSubTypes.Type(name = "FaithPointCheatCommand", value = FaithPointCheatCommand.class)
})
public abstract class Command {
    /**
     * The command to execute on a player action interface
     * @param player the player on which execute the command
     */
    public abstract void execute(PlayerAction player);

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
