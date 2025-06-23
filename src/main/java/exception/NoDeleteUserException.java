package exception;

public class NoDeleteUserException extends Exception {
    public NoDeleteUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
