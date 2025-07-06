package myApp.exception;

public class NoUpdateUserException extends RuntimeException {
    public NoUpdateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
