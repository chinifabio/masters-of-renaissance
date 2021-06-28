package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This class represents the requisite to activate a Card
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "ResourceReq", value = LiteResourceRequisite.class),
        @JsonSubTypes.Type(name = "CardReq", value = LiteCardRequisite.class),
        @JsonSubTypes.Type(name = "ColorCardReq", value = LiteColorCardRequisite.class)
})
public abstract class LiteRequisite {

    /**
     * This method is used to print the requisite in CLI
     * @param leaderCard to print
     * @param x horizontal position
     * @param y vertical position
     */
    @JsonIgnore
    public abstract void printRequisite(String[][] leaderCard, int x, int y);

}
