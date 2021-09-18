package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Project;

public class ProjectResponse {

    public static class ProjectData extends DataResponse<Project> {
        public ProjectData(boolean status, Project data) {
            super(status, data);
        }
    }

    public static class ProjectContent extends ContentResponse<Project> {
        public ProjectContent(boolean status, Project content, Paging paging) {
            super(status, content, paging);
        }

        public ProjectContent(boolean status, Project content) {
            super(status, content);
        }
    }
}
