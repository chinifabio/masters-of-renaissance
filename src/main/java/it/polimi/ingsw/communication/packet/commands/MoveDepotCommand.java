package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This command moves the resources between depots
 */
public class MoveDepotCommand extends Command{

    /**
     * The source of the resource to move
     */
    private final DepotSlot from;

    /**
     * The destination of the resource to move
     */
    private final DepotSlot dest;

    /**
     * The resource to move between the depot
     */
    private final Resource loot;

    /**
     * This is the constructor of the class
     * @param from is the depot where the resource is taken
     * @param dest is the depot where the resoruce will be placed
     * @param loot is the resource to move
     */
    @JsonCreator
    public MoveDepotCommand(@JsonProperty("from") DepotSlot from, @JsonProperty("dest") DepotSlot dest, @JsonProperty("loot") Resource loot) {
        this.from = from;
        this.dest = dest;
        this.loot = loot;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public Packet execute(PlayerAction player) {
        return player.moveBetweenDepot(this.from, this.dest, this.loot);
    }
}
