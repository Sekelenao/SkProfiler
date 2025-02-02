package io.github.sekelenao.skprofiler.exception;

public class InvalidJsonException extends Exception {
    public InvalidJsonException(Throwable cause) {
        super(cause);
    }
    public InvalidJsonException(String message) {
        super(message);
    }
}
