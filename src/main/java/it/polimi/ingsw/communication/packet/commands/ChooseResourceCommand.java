package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This command permits to the player to choose a resource at the start of the match
 */
public class ChooseResourceCommand extends Command{
    /**
     * The resource to obtain at the start of the match
     */
    private final ResourceType res;

    /**
     * The depot where place the choosen resource
     */
    private final DepotSlot dest;

    @JsonCreator
    public ChooseResourceCommand(@JsonProperty("dest") DepotSlot dest, @JsonProperty("res") ResourceType res) {
        this.res = res;
        this.dest = dest;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public Packet execute(PlayerAction player) {
        return player.chooseResource(this.dest, this.res);
    }
}
