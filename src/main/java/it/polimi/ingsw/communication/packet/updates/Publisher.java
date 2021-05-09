package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.view.litemodel.LiteModel;

/**
 * This class contains a method that take as input a lite model and than depending on the implementation update it
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "LeaderCard", value = LeaderCardPublisher.class),
        @JsonSubTypes.Type(name = "Tray", value = TrayPublisher.class),
        @JsonSubTypes.Type(name = "BuildMe", value = BuildMePublisher.class)
})
public abstract class Publisher {
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
