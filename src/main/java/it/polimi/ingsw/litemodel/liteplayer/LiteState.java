package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "CountingPoints", value = CountingPoints.class),
        @JsonSubTypes.Type(name = "LeaderSelection", value = LeaderSelection.class),
        @JsonSubTypes.Type(name = "MainActionDone", value = MainActionDone.class),
        @JsonSubTypes.Type(name = "NoActionDone", value = NoActionDone.class),
        @JsonSubTypes.Type(name = "NotHisTurn", value = NotHisTurn.class),
        @JsonSubTypes.Type(name = "PendingStart", value = PendingStart.class),
})
public abstract class LiteState {
    protected final List<Actions> clickable = new ArrayList<>();
    public LiteState(List<Actions> list) {
        this.clickable.addAll(list);
    }

    public boolean canDoAction(Actions act) {
        return this.clickable.contains(act);
    }

    @Override
    public String toString() {
        return "LiteState{" + "clickable=" + clickable + '}';
    }
}
