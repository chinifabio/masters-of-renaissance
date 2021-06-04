package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.*;
import it.polimi.ingsw.view.View;

import java.util.List;

public class DiscountUpdater extends Updater{
    private final String nickname;

    private final List<Resource> list;

    @JsonCreator
    public DiscountUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("list") List<Resource> list) {
        this.nickname = nickname;
        this.list = list;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setDiscounts(this.nickname, ResourceBuilder.mapResource(this.list));
    }
}
