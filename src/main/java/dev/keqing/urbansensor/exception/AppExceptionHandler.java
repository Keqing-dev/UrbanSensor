package dev.keqing.urbansensor.exception;

import dev.keqing.urbansensor.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler
    ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ExceptionHandler(value = {CustomException.class})
    ResponseEntity<?> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getCode()).body(null);
    }
}
