package dev.keqing.urbansensor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Report;

import java.util.List;

public class ReportsResponse extends StatusResponse {
    private List<Report> content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Paging paging;

    public ReportsResponse(boolean success, List<Report> content, Paging paging) {
        super(success);
        this.content = content;
        this.paging = paging;
    }

    public List<Report> getContent() {
        return content;
    }

    public void setContent(List<Report> content) {
        this.content = content;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
