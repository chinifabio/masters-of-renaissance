package it.polimi.ingsw.view.litemodel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LiteFaithTrack {

    private final List<LiteCell> track;

    public LiteFaithTrack() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        this.track = objectMapper.readValue(
                    new File("src/resources/FaithTrack.json"),
                    new TypeReference<List<LiteCell>>(){});
    }


    public boolean isVaticanSpace(int cell){
        return track.get(cell).getVaticanSpace() != VaticanSpace.NONE;
    }


    public boolean isPopeSpace(int cell){
        return track.get(cell).isPopeSpace().equals("PopeSpace");
    }


    public int getSizeTrack(){
        return this.track.size();
    }


    public int getVictoryPointCell(int cell){
        return this.track.get(cell).getVictoryPoints();
    }
}
