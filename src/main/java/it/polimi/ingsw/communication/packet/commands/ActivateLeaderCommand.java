package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * This command activate a leader card from the passed id.
 */
public class ActivateLeaderCommand extends Command{

    /**
     * The id of the card to activate
     */
    private final String card;

    /**
     * This is the constructor of the class
     * @param card is the id of the card that will be activated
     */
    @JsonCreator
    public ActivateLeaderCommand(@JsonProperty("card") String card) {
        this.card = card;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public void execute(PlayerAction player) {
        player.activateLeaderCard(this.card);
    }
}
