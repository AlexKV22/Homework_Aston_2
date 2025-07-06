package myApp.exception;

public class NoSaveNewUserException extends RuntimeException {
    public NoSaveNewUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
