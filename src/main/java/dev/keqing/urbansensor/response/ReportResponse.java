package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.projection.ReportSummary;

import java.util.List;

public class ReportResponse extends StatusResponse {
    private Report data;

    public ReportResponse(boolean success, Report data) {
        super(success);
        this.data = data;
    }

    public Report getData() {
        return data;
    }

    public void setData(Report data) {
        this.data = data;
    }

    public static class ReportData extends StatusResponse {
        private ReportSummary data;

        public ReportData(boolean success, ReportSummary data) {
            super(success);
            this.data = data;
        }

        public ReportSummary getData() {
            return data;
        }

        public void setData(ReportSummary data) {
            this.data = data;
        }
    }

    public static class ReportContent extends StatusResponse {
        private List<ReportSummary> content;
        private Paging paging;

        public ReportContent(boolean success, List<ReportSummary> content) {
            super(success);
            this.content = content;
        }

        public List<ReportSummary> getContent() {
            return content;
        }

        public void setContent(List<ReportSummary> content) {
            this.content = content;
        }

        public Paging getPaging() {
            return paging;
        }

        public void setPaging(Paging paging) {
            this.paging = paging;
        }
    }
}


