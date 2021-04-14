package it.polimi.ingsw.model.exceptions.productionException;

import it.polimi.ingsw.model.resource.Resource;

import java.util.List;

public class IllegalTypeInProduction extends Exception{
    /**
     * the required resource list of wrong production
     */
    private Resource illegal;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public IllegalTypeInProduction(Resource x) {
        this.illegal = x;
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println("NormalProduction got an ILLEGAL res: " + this.illegal);
    }
}
