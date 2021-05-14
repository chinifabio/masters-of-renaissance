package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;

public class LiteResourceRequirements extends LiteRequirementsType {

    private LiteResource resource;

    @JsonCreator
    public LiteResourceRequirements(@JsonProperty("resource") LiteResource resource) {
        this.resource = resource;
    }

    public LiteResource getResource() {
        return resource;
    }
}
