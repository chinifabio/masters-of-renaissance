package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import it.polimi.ingsw.model.resource.ResourceType;

/**
 * Build all the available type of marbles
 */
public class MarbleBuilder {
    /**
     * Build a blue marble
     * @return blue marble
     */
    public static Marble buildBlue() {
        return new Marble(MarbleColor.BLUE, ResourceType.SHIELD);
    }

    /**
     * Build a yellow marble
     * @return yellow marble
     */
    public static Marble buildYellow() {
        return new Marble(MarbleColor.YELLOW, ResourceType.COIN);
    }

    /**
     * Build a gray marble
     * @return gray marble
     */
    public static Marble buildGray() {
        return new Marble(MarbleColor.GRAY, ResourceType.STONE);
    }

    /**
     * Build a purple marble
     * @return purble marble
     */
    public static Marble buildPurple() {
        return new Marble(MarbleColor.PURPLE, ResourceType.SERVANT);
    }

    /**
     * Build a white marble
     * @return white marble
     */
    public static Marble buildWhite() {
        return new PaintableMarble(MarbleColor.WHITE);
    }

    /**
     * Build a red marble
     * @return red marble
     */
    public static Marble buildRed() {
        return new Marble(MarbleColor.RED, ResourceType.FAITHPOINT);
    }
}
//Todo remove (?)
