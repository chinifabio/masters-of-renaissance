package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.List;

public class PlayerOrderUpdater extends Updater{

    List<String> players;

    @JsonCreator
    public PlayerOrderUpdater(@JsonProperty("Players") List<String> players) {
        this.players = players;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setPlayerOrder(players);
    }
}
