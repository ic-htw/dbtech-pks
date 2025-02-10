package de.htwberlin.dbtech.exceptions;

/**
 * @author Ingo Classen
 */
public class DataException extends RuntimeException {

    /**
     * Erzeugt eine DataException.
     */
    public DataException() {
    }

    /**
     * Erzeugt eine DataException mit einer Nachricht.
     *
     * @param msg - die Nachricht
     */
    public DataException(String msg) {
        super(msg);
    }

    /**
     * Erzeugt eine DataException und verweist auf ein Throwable t.
     *
     * @param t - das Throwable.
     */
    public DataException(Throwable t) {
        super(t);
    }

    /**
     * Erzeugt eine ServiceException mit einer Nachricht und verweist auf ein Throwable t.
     *
     * @param msg - die Nachricht
     * @param t   - das Throwable.
     */
    public DataException(String msg, Throwable t) {
        super(msg, t);
    }

}