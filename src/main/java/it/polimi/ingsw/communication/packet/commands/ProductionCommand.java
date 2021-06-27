package it.polimi.ingsw.communication.packet.commands;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This packet activate the production in the warehouse
 */
public class ProductionCommand extends Command{
    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.production();
    }
}
