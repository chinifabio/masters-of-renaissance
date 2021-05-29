package it.polimi.ingsw.model.exceptions.warehouse.production;

import it.polimi.ingsw.model.resource.Resource;

public class IllegalTypeInProduction extends Exception{

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public IllegalTypeInProduction(Resource x) {
        super("NormalProduction received an ILLEGAL res: " + x);
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(super.getMessage());
    }
}
