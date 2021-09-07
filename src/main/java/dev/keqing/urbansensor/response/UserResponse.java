package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Users;

public class UserResponse extends StatusResponse{
    public Users data;

    public UserResponse(boolean success, Users data) {
        super(success);
        this.data = data;
    }

    public Users getData() {
        return data;
    }

    public void setData(Users data) {
        this.data = data;
    }
}
