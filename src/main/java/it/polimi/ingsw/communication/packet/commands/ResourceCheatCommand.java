package it.polimi.ingsw.communication.packet.commands;

import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This packet activate the cheat for obtaining 50 for each storable resource
 */
public class ResourceCheatCommand extends Command{
    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.resourceCheat();
    }
}
