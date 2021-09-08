package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.UserProject;

import java.util.List;

public class UserProjectResponse extends StatusResponse {
    public UserProject data;
    public List<UserProject> content;

    public UserProjectResponse(boolean success, UserProject data) {
        super(success);
        this.data = data;
    }

    public UserProjectResponse(boolean success, List<UserProject> data) {
        super(success);
        this.content = data;
    }
}
