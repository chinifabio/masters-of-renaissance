package it.polimi.ingsw.communication.packet.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;

import javax.swing.*;
import java.util.ArrayList;

/**
 * This command allow the player to buy a dev card in the dev setup
 */
public class BuyDevCardCommand extends Command {

    /**
     * This attribute is the row of the cards' grid
     */
    private final LevelDevCard row;

    /**
     * This attribute is the column of the cards' grid
     */
    private final ColorDevCard col;

    /**
     * This attribute is the slot where the card will be placed
     */
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
