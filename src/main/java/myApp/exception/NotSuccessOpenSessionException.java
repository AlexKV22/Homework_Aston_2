package myApp.exception;

public class NotSuccessOpenSessionException extends RuntimeException {
    public NotSuccessOpenSessionException(String message) {
        super(message);
    }
}
