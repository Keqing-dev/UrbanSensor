package dev.keqing.urbansensor.response;

public class MessageResponse extends StatusResponse {
    private String message;

    public MessageResponse(boolean success, String message) {
        super(success);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
