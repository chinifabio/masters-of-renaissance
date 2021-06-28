package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.view.cli.CLI;

/**
 * This class represents on of the effect of the cards
 */
public class LiteWhiteMarbleEffect extends LiteEffect{

    /**
     * This attribute is the possible LiteMarble conversion
     */
    private final LiteMarble marble;

    /**
     * This is the constructor of the class:
     * @param marble to which white marble can be converted to
     */
    @JsonCreator
    public LiteWhiteMarbleEffect(@JsonProperty("marble") LiteMarble marble) {
        this.marble = marble;

    }

    /**
     * This method returns the marble of the possible conversion
     * @return the possible LiteMarble conversion
     */
    public LiteMarble getMarble() {
        return marble;
    }

    /**
     * This method is used to print the effect in cli
     * @param leaderCard to print
     * @param x horizontal position
     * @param y vertical position
     */
    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {
        leaderCard[x + 4][y + 3] = "O";
        leaderCard[x + 4][y + 5] = "=";
        leaderCard[x + 4][y + 7] = CLI.colorResource.get(marble.getToResource());
    }
}
