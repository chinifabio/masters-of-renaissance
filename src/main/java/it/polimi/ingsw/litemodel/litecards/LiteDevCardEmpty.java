package it.polimi.ingsw.litemodel.litecards;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class LiteDevCardEmpty extends LiteDevCard{

    private final LevelDevCard level;

    private final ColorDevCard color;

    public LiteDevCardEmpty(LevelDevCard level1, ColorDevCard color1) {
        super("Empty", null, 0, level1, color1, null);
        this.level = level1;
        this.color = color1;
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
