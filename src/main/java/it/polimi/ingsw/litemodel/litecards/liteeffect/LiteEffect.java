package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "AddDepot", value = LiteAddDepotEffect.class),
        @JsonSubTypes.Type(name = "AddDiscount", value = LiteAddDiscountEffect.class),
        @JsonSubTypes.Type(name = "WhiteMarble", value = LiteWhiteMarbleEffect.class),
        @JsonSubTypes.Type(name = "AddExtraProduction", value = LiteAddProductionEffect.class),
        @JsonSubTypes.Type(name = "AddProduction", value = LiteAddProductionEffect.class),
        @JsonSubTypes.Type(name = "ShuffleMoveOne", value = LiteShuffleMoveOneEffect.class),
        @JsonSubTypes.Type(name = "MoveTwo", value = LiteMoveTwoEffect.class),
        @JsonSubTypes.Type(name = "DestroyCard", value = LiteDestroyCardsEffect.class),
})
public abstract class LiteEffect {

    @JsonIgnore
    public abstract void printEffect(String[][] leaderCard, int x, int y);

}
