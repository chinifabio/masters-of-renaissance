package it.polimi.ingsw.model.exceptions.warehouse.production;

import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;

import static it.polimi.ingsw.TextColors.*;

public class IllegalNormalProduction extends Exception{

    private final NormalProduction err;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param errm the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IllegalNormalProduction(NormalProduction err, String errm) {
        System.out.println(errm);
        this.err = err;
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "Something went wrong with this production: " + RESET + this.err));
    }
}
