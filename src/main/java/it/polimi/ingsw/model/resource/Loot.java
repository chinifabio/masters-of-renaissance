package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public abstract class Loot {
    protected int amount;

    public int getAmount() {
        return amount;
    }

    public abstract Resource getType();

    public abstract LevelDevCard getLevel();

    public abstract ColorDevCard getColor();
}
