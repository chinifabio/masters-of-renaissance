package it.polimi.ingsw.model.exceptions.tray;

public class OutOfBoundMarketTrayException extends Exception{

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public OutOfBoundMarketTrayException() {
        super("The limit of the Market Tray has been exceeded");
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
