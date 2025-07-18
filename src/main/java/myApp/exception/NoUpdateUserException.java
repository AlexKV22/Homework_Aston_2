package myApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NoUpdateUserException extends RuntimeException {
    public NoUpdateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
