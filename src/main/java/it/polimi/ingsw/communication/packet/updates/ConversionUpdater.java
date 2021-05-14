package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;

import java.util.List;
import java.util.stream.Collectors;

public class ConversionUpdater extends Updater {
    private final List<Marble> list;
    private final String nickname;

    @JsonCreator
    public ConversionUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("list") List<Marble> marbleConversions) {
        this.nickname = nickname;
        this.list = marbleConversions;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setConversions(this.nickname, this.list.stream().map(x->x.color()).collect(Collectors.toList()));
    }
}
