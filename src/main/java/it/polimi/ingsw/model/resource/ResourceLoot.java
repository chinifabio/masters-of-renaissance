package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class ResourceLoot extends Loot {
    private Resource resource;

    public ResourceLoot(Resource resource, int i) {
        this.resource = resource;
        this.amount = i;
    }

    @Override
    public Resource getType() {
        return resource;
    }

    @Override
    public LevelDevCard getLevel() {
        return null; // need to throw exception
    }

    @Override
    public ColorDevCard getColor() {
        return null; // need to throw exception
    }
}
