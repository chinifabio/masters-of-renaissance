package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

public class SetNumberCommand extends Command {
    private int size;

    @JsonCreator
    public SetNumberCommand(@JsonProperty("size") int size) {
        this.size = size;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public Packet execute(PlayerAction player) {
        return player.setPlayerNumber(this.size);
    }
}
