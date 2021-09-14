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
    ResponseEntity<CommonResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponse(false,
                ex.getMessage()));
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    ResponseEntity<CommonResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(value = {CustomException.class})
    ResponseEntity<CommonResponse> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getCode()).body(new CommonResponse(false, ex.getMessage()));
    }
}
