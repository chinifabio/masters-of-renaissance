package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

public class FaithPointCheatCommand extends Command{

    /**
     * The number of position the player will move on the faith track
     */
    private int fp;

    /**
     * This is the constructor of the class
     * @param fp is the number of position the player will move on the faith track
     */
    @JsonCreator
    public FaithPointCheatCommand(@JsonProperty("fp") int fp) {
        this.fp = fp;
    }

    @Override
    public Packet execute(PlayerAction player) {
        return player.fpCheat(fp);
    }
}
