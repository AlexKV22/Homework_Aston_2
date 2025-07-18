package myApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NoSaveNewUserException extends RuntimeException {
    public NoSaveNewUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
