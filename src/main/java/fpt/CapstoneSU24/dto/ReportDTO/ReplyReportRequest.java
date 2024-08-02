package fpt.CapstoneSU24.dto.ReportDTO;

public class ReplyReportRequest {
    private int reportId;
    private String responseDetail;

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getResponseDetail() {
        return responseDetail;
    }

    public void setResponseDetail(String responseDetail) {
        this.responseDetail = responseDetail;
    }
}
