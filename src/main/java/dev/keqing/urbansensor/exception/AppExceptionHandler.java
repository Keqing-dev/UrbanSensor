package dev.keqing.urbansensor.exception;

import dev.keqing.urbansensor.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler
    ResponseEntity<MessageResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(false,
                ex.getMessage()));
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    ResponseEntity<MessageResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(value = {CustomException.class})
    ResponseEntity<MessageResponse> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getCode()).body(new MessageResponse(false, ex.getMessage()));
    }
}
