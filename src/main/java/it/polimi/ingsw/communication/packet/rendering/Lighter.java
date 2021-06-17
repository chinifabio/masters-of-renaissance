package it.polimi.ingsw.communication.packet.rendering;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.view.View;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "GameSession", value = FireGameSession.class),
        @JsonSubTypes.Type(name = "GameResult", value = FireGameResult.class),
        @JsonSubTypes.Type(name = "GameInit", value = FireGameInit.class),
        @JsonSubTypes.Type(name = "GameEnded", value = FireGameEnded.class),
        @JsonSubTypes.Type(name = "LobbyWait", value = FireLobbyWait.class),
        @JsonSubTypes.Type(name = "GameCreator", value = FireGameCreator.class),
})
public abstract class Lighter {
    public abstract void fire(View view);

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
