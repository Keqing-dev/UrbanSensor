package dev.keqing.urbansensor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.UserProject;

import java.util.List;

public class UserProjectResponse extends StatusResponse {
    public UserProject data;
    public List<UserProject> content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Paging paging;

    public UserProjectResponse(boolean success, UserProject data) {
        super(success);
        this.data = data;
    }

    public UserProjectResponse(boolean success, List<UserProject> data) {
        super(success);
        this.content = data;
    }


    public UserProjectResponse(boolean success, List<UserProject> content,Paging paging) {
        super(success);
        this.content = content;
        this.paging = paging;
    }

    public UserProject getData() {
        return data;
    }

    public void setData(UserProject data) {
        this.data = data;
    }

    public List<UserProject> getContent() {
        return content;
    }

    public void setContent(List<UserProject> content) {
        this.content = content;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
