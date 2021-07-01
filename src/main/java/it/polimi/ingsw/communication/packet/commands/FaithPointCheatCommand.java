package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.PlayerAction;

public class FaithPointCheatCommand extends Command{

    /**
     * The number of position the player will move on the faith track
     */
    private final int fp;

    /**
     * This is the constructor of the class
     * @param fp is the number of position the player will move on the faith track
     */
    @JsonCreator
    public FaithPointCheatCommand(@JsonProperty("fp") int fp) {
        this.fp = fp;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.fpCheat(fp);
    }
}
