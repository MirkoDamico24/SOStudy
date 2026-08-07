package it.uniroma2.dicii.ispw.sostudy.exception;

public class ModelException extends RuntimeException {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
