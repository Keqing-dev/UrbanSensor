package dev.keqing.urbansensor.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Hidden
@RestController
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<?> error(HttpServletRequest request) {
        int code = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message;

        switch (code) {
            case 400:
                message = HttpStatus.BAD_REQUEST.getReasonPhrase();
                break;
            case 401:
                message = HttpStatus.UNAUTHORIZED.getReasonPhrase();
                break;
            case 403:
                message = HttpStatus.FORBIDDEN.getReasonPhrase();
                break;
            case 404:
                message = HttpStatus.NOT_FOUND.getReasonPhrase();
                break;
            case 500:
                message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
                break;
            default:
                message = "An error occurred";
        }

        return ResponseEntity.status(code).body(null);
    }
}
