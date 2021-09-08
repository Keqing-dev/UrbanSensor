package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Report;

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
}


