package it.polimi.ingsw.communication.packet.commands;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.player.PlayerAction;

public class ProductionCommand extends Command{
    @Override
    public Packet execute(PlayerAction player) {
        return player.production();
    }
}
