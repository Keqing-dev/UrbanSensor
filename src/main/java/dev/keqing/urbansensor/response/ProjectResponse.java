package dev.keqing.urbansensor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Project;

import java.util.List;

public class ProjectResponse extends StatusResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Project data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Paging paging;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Project> content;

    public ProjectResponse(boolean success, Project data) {
        super(success);
        this.data = data;
    }

    public ProjectResponse(boolean success, List<Project> data) {
        super(success);
        this.content = data;
    }

    public ProjectResponse(boolean success, List<Project> data, Paging paging) {
        super(success);
        this.content = data;
        this.paging = paging;
    }

    public Project getData() {
        return data;
    }

    public void setData(Project data) {
        this.data = data;
    }

    public List<Project> getContent() {
        return content;
    }

    public void setContent(List<Project> content) {
        this.content = content;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
