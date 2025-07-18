package myApp.exception;

public class UserNotReadException extends RuntimeException {
    public UserNotReadException(String message) {
        super(message);
    }
    public UserNotReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
