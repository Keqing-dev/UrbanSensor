package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Project;

import java.util.List;

public class ProjectResponse extends StatusResponse{
    public Project data;
    public List<Project> content;

    public ProjectResponse(boolean success, Project data) {
        super(success);
        this.data = data;
    }


    public ProjectResponse(boolean success, List<Project> data) {
        super(success);
        this.content = data;
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
}
