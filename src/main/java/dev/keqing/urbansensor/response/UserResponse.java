package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.User;

import java.util.List;

public class UserResponse {
    public static class UserData extends StatusResponse {
        private User data;

        public UserData(boolean success, User data) {
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

    public static class UserContent extends StatusResponse {
        private List<User> content;

        public UserContent(boolean success, List<User> content) {
            super(success);
            this.content = content;
        }

        public List<User> getContent() {
            return content;
        }

        public void setContent(List<User> content) {
            this.content = content;
        }
    }
}
