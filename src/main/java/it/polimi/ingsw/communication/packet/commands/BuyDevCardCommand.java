package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;

/**
 * This command allow the player to buy a dev card in the dev setup
 */
public class BuyDevCardCommand extends Command {
    private final LevelDevCard row;
    private final ColorDevCard col;
    private final DevCardSlot destination;

    /**
     * Create a command that buy the dev card indicated by parameters and place it in the destination
     * @param row the row of the dev setup
     * @param col the col of the dev setup
     * @param destination the destination where put the obtained card
     */
    @JsonCreator
    public BuyDevCardCommand(@JsonProperty("row") LevelDevCard row, @JsonProperty("col") ColorDevCard col, @JsonProperty("destination") DevCardSlot destination) {
        this.row = row;
        this.col = col;
        this.destination = destination;
    }

    /**
     * The command to execute on a player action interface
     *
     * @param player the player on which execute the command
     */
    @Override
    public Packet execute(PlayerAction player) {
        return player.buyDevCard(this.row, this.col, this.destination);
    }
}
