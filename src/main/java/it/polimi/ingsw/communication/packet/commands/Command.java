package it.polimi.ingsw.communication.packet.commands;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This interface is used to contain a command to be execute on a playerAction interface
 */
public interface Command {
    /**
     * The command to execute on a player action interface
     * @param player the player on which execute the command
     */
    Packet execute(PlayerAction player);
}
