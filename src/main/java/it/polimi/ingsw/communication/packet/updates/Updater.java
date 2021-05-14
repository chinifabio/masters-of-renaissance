package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.LiteModel;

/**
 * This class contains a method that take as input a lite model and than depending on the implementation update it
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Conversion", value = ConversionUpdater.class),
        @JsonSubTypes.Type(name = "Depot", value = DepotUpdater.class),
        @JsonSubTypes.Type(name = "Develop", value = DevelopUpdater.class),
        @JsonSubTypes.Type(name = "DevSetup", value = DevSetupUpdater.class),
        @JsonSubTypes.Type(name = "Discount", value = DiscountUpdater.class),
        @JsonSubTypes.Type(name = "FaithTrack", value = FaithTrackUpdater.class),
        @JsonSubTypes.Type(name = "Leader", value = LeaderUpdater.class),
        @JsonSubTypes.Type(name = "Lorenzo", value = LorenzoUpdater.class),
        @JsonSubTypes.Type(name = "NewPlayer", value = NewPlayerUpdater.class),
        @JsonSubTypes.Type(name = "Production", value = ProductionUpdater.class),
        @JsonSubTypes.Type(name = "Token", value = TokenUpdater.class),
        @JsonSubTypes.Type(name = "Tray", value = TrayUpdater.class),
        @JsonSubTypes.Type(name = "PlayerState", value = PlayerStateUpdater.class),
})
public abstract class Updater {

    /**
     * Take a lite model as input and apply to the implementing function
     * @param liteModel the lite model on the client
     */
    public abstract void update(LiteModel liteModel);

    /**
     * Return a json representation of the command
     * @return json serialized string of the command
     */
    public String jsonfy(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
