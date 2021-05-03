package it.polimi.ingsw.communication.packet.commands;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This command allow the player to paint a marble in the tray whit one of his conversion
 */
public class PaintMarbleCommand implements Command {
    /**
     * The index of the conversion
     */
    private final int conversion;

    /**
     * the index of the marble in the tray to paint
     */
    private final int index;

    /**
     * Build a command that paint the index th marble in the tray
     * @param conversion the id of the conversion
     * @param index the index of the mable
     */
    public PaintMarbleCommand(int conversion, int index) {
        this.conversion = conversion;
        this.index = index;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public Packet execute(PlayerAction player) {
        return player.paintMarbleInTray(this.conversion, this.index);
    }
}
