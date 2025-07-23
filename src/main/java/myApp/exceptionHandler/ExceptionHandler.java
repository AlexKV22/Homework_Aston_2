package myApp.exceptionHandler;

import myApp.exception.NoDeleteUserException;
import myApp.exception.NoSaveNewUserException;
import myApp.exception.NoUpdateUserException;
import myApp.exception.UniqueFieldException;
import myApp.exception.UserNotFoundException;
import myApp.exception.UserNotReadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSaveNewUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNoSaveNewUserException(NoSaveNewUserException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoFoundUserException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoUpdateUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNoUpdateUserException(NoUpdateUserException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotReadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNoUpdateUserException(UserNotReadException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoDeleteUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNoDeleteUserException(NoDeleteUserException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> validateException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UniqueFieldException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> uniqueFieldException(UniqueFieldException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
