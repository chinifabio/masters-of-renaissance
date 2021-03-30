package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class CardLoot extends Loot{
    private LevelDevCard level;
    private ColorDevCard color;

    public CardLoot(LevelDevCard level, ColorDevCard color, int i) {
        this.level = level;
        this.color = color;
        this.amount = i;
    }

    @Override
    public Resource getType() {
        return null; // need to throw exception
    }

    @Override
    public LevelDevCard getLevel() {
        return level;
    }

    @Override
    public ColorDevCard getColor() {
        return color;
    }
}
