package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;

public class MoveInProductionCommand extends Command {

    /**
     * The loot to move in a production
     */
    private final Resource loot;

    /**
     * the production destination
     */
    private final ProductionID dest;

    /**
     * the source of the resource to move
     */
    private final DepotSlot from;

    /**
     * This is the constructor of the class
     * @param from is the depot where the resource is taken from
     * @param dest is the production where the resource will be placed
     * @param loot is the resource to move
     */
    @JsonCreator
    public MoveInProductionCommand(@JsonProperty("from") DepotSlot from, @JsonProperty("dest") ProductionID dest, @JsonProperty("loot") Resource loot){
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
        return player.moveInProduction(this.from, this.dest, this.loot);
    }
}
