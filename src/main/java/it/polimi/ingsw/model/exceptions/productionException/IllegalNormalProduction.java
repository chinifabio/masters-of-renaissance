package it.polimi.ingsw.model.exceptions.productionException;

import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;

public class IllegalNormalProduction extends Exception{

    private NormalProduction err;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
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
        System.out.println("exception - " + this.err);
    }
}
