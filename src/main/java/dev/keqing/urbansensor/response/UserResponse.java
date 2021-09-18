package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.User;

public class UserResponse {
    public static class UserData extends DataResponse<User> {
        public UserData(boolean status, User data) {
            super(status, data);
        }
    }

    public static class UserContent extends ContentResponse<User> {
        public UserContent(boolean status, User content, Paging paging) {
            super(status, content, paging);
        }
    }
}
