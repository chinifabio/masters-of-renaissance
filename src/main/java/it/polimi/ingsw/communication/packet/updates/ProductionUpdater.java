package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

public class ProductionUpdater extends Updater{
    private final String nickname;

    private final LiteProduction prod;

    private final ProductionID id;

    @JsonCreator
    public ProductionUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("prod") LiteProduction prod, @JsonProperty("id") ProductionID id) {
        this.nickname = nickname;
        this.prod = prod;
        this.id = id;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setProduction(nickname, prod, id);
    }
}
