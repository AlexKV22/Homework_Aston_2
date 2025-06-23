package exception;

public class NoSaveNewUserException extends Exception {
    public NoSaveNewUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
