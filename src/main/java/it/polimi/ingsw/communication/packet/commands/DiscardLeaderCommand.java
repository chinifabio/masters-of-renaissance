package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This command discard a leader card owned by the player
 */
public class DiscardLeaderCommand extends Command {

    /**
     * The id of the leader to discard
     */
    private final String leaderID;

    /**
     * Create an instance of the command to discard a leader card of the player
     * @param leaderID the id of the card
     */
    @JsonCreator
    public DiscardLeaderCommand(@JsonProperty("leaderID") String leaderID) {
        this.leaderID = leaderID;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.discardLeader(this.leaderID);
    }

    /**
     * @return the id of the leaderCard
     */
    @JsonGetter("leaderID")
    public String getLeaderID() {
        return leaderID;
    }
}
