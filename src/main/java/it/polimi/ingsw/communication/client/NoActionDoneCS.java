package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.BuyDevCardCommand;
import it.polimi.ingsw.communication.packet.commands.DiscardLeaderCommand;
import it.polimi.ingsw.communication.packet.commands.PaintMarbleCommand;
import it.polimi.ingsw.communication.packet.commands.UseMarketTrayCommand;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;

public class NoActionDoneCS extends ClientState{

    public NoActionDoneCS(Client context) {
        super(context, "invalid action, choose another one");
    }

    @Override
    public Packet buyDevCard() {
        LevelDevCard level = LevelDevCard.LEVEL1;
        ColorDevCard color = ColorDevCard.GREEN;
        DevCardSlot slot = DevCardSlot.LEFT;

        //metodi cli/gui per cambiare i valori

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand(level,color,slot).jsonfy());
    }

    @Override
    public Packet discardLeader() {
        String id = "000";

        //metodi cli/gui per cambiare il valore

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand(id).jsonfy());
    }

    @Override
    public Packet useMarketTray() {
        RowCol rc = RowCol.ROW;
        int index = 0;

        //metodi cli/gui per cambiare i valori

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(rc,index).jsonfy());
    }

    @Override
    public Packet paintMarbleColor() {

        int conversion = 0, index =0;

        //metodi cli/gui per cambiare i valori

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(conversion,index).jsonfy());
    }


}
