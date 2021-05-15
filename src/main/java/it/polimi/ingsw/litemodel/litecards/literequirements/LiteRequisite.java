package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "ResourceReq", value = LiteResourceRequisite.class),
        @JsonSubTypes.Type(name = "CardReq", value = LiteCardRequisite.class),
        @JsonSubTypes.Type(name = "ColorCardReq", value = LiteColorCardRequisite.class)
})
public abstract class LiteRequisite {

    @JsonIgnore
    public abstract void printRequisite(String[][] leaderCard, int x, int y);

}
