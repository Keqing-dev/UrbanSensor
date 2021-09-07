package dev.keqing.urbansensor.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    private HttpStatus code;

    public CustomException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(HttpStatus code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }
}
