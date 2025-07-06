package myApp.exception;

public class CreateJDBCConnectionException extends RuntimeException {
    public CreateJDBCConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
