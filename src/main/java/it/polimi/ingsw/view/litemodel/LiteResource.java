package it.polimi.ingsw.view.litemodel;

import it.polimi.ingsw.model.resource.ResourceType;

public class LiteResource {

    private final ResourceType type;

    private final int amount;

    public LiteResource(ResourceType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ResourceType getType(){
        return this.type;
    }

    public int getAmount() {
        return this.amount;
    }
}
