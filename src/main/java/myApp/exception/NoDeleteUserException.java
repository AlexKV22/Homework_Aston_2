package myApp.exception;

public class NoDeleteUserException extends RuntimeException {
    public NoDeleteUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDeleteUserException(String message) {
        super(message);
    }
}
