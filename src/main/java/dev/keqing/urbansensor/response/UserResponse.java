package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.User;

public class UserResponse extends StatusResponse{
    public User data;

    public UserResponse(boolean success, User data) {
        super(success);
        this.data = data;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
