package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Paging;
import dev.keqing.urbansensor.projection.ReportSummary;


public class ReportResponse {
    public static class ReportData extends DataResponse<ReportSummary> {
        public ReportData(boolean status, ReportSummary data) {
            super(status, data);
        }
    }

    public static class ReportContent extends ContentResponse<ReportSummary> {
        public ReportContent(boolean status, ReportSummary content, Paging paging) {
            super(status, content, paging);
        }
    }
}


