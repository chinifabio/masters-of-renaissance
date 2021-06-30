package it.polimi.ingsw.communication.packet.clientexecutable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.clientexecutable.*;
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

        @JsonSubTypes.Type(name = "Model", value = ModelUpdater.class),
        @JsonSubTypes.Type(name = "Lorenzo", value = LorenzoPopUp.class),

        @JsonSubTypes.Type(name = "Message", value = PlayerMessage.class),
        @JsonSubTypes.Type(name = "Error", value = PlayerError.class),
})
/**
 * This class
 */
public abstract class ClientExecutable {
    /**
     * Execute a command on the view
     * @param view the view on with execute the command
     */
    public abstract void execute(View view);

    /**
     * Return a json representation of the command
     * @return json serialized string of the command
     */
    public final String jsonfy(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}"; // causes an exception that close the client
        }
    }
}
