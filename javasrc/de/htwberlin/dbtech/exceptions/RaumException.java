package de.htwberlin.dbtech.exceptions;

public class RaumException extends RuntimeException {

    public RaumException() {
    }

    public RaumException(Throwable t) {
        super(t);
    }

    public RaumException(String msg) {
        super(msg);
    }

    public RaumException(String msg, Throwable t) {
        super(msg, t);
    }

}
