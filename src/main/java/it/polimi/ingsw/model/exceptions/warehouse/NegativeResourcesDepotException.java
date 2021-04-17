package it.polimi.ingsw.model.exceptions.warehouse;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.NormalDepot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.SpecialDepot;

import static it.polimi.ingsw.TextColors.*;
import static it.polimi.ingsw.TextColors.RED_BRIGHT;

public class NegativeResourcesDepotException extends Exception {

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public NegativeResourcesDepotException() {
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "This Depot will have a negative number of resources!"));
    }
}
