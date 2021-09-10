package dev.keqing.urbansensor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.projection.ReportSummary;

import java.util.List;

public class ReportsResponse extends StatusResponse {
    private List<Object> content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Paging paging;

    public ReportsResponse(boolean success, List<Object> content, Paging paging) {
        super(success);
        this.content = content;
        this.paging = paging;
    }

    public List<Object> getContent() {
        return content;
    }

    public void setContent(List<Object> content) {
        this.content = content;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
