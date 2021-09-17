package dev.keqing.urbansensor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Project;

import java.util.List;

public class ProjectResponse {

    public static class ProjectData extends StatusResponse {
        private Project data;

        public ProjectData(boolean success, Project data) {
            super(success);
            this.data = data;
        }

        public Project getData() {
            return data;
        }

        public void setData(Project data) {
            this.data = data;
        }
    }

    public static class ProjectContent extends StatusResponse {
        private List<Project> content;
        private Paging paging;
        private Long maxItems;


        public ProjectContent(boolean success, List<Project> content, Paging paging) {
            super(success);
            this.content = content;
            this.paging = paging;
        }

        public ProjectContent(boolean success, List<Project> content, Paging paging, Long maxItems) {
            super(success);
            this.content = content;
            this.paging = paging;
            this.maxItems = maxItems;

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

        public Long getMaxItems() {
            return maxItems;
        }

        public void setMaxItems(Long maxItems) {
            this.maxItems = maxItems;
        }
    }
}
