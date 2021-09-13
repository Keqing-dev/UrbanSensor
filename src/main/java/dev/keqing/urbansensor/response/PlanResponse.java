package dev.keqing.urbansensor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Plan;

import java.util.List;

public class PlanResponse extends StatusResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Plan data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Paging paging;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Plan> content;

    public PlanResponse(Boolean success, Plan data) {
        super(success);
        this.data = data;
    }

    public PlanResponse(boolean success, List<Plan> content) {
        super(success);
        this.content = content;
    }
    public PlanResponse(boolean success, List<Plan> content,Paging paging) {
        super(success);
        this.paging = paging;
        this.content = content;
    }

    public Plan getData() {
        return data;
    }

    public void setData(Plan data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<Plan> getContent() {
        return content;
    }

    public void setContent(List<Plan> content) {
        this.content = content;
    }
}
