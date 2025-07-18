package myApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NoDeleteUserException extends RuntimeException {
    public NoDeleteUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDeleteUserException(String message) {
        super(message);
    }
}
