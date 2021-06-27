package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This command activate the production of the passed player
 */
public class ActivateProductionCommand extends Command{

    /**
     * This is the constructor of the class
     */
    @JsonCreator
    public ActivateProductionCommand(){}

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.activateProductions();
    }
}
