package it.polimi.ingsw.communication.packet.commands;

import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This command allow the player to end his turn
 */
public class EndTurnCommand extends Command{
    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.endThisTurn();
    }
}
