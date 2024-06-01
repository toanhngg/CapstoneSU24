package fpt.CapstoneSU24.dto.B02;

public class B02_GetListReport {
    private int reportId;
    private int status;
    private String code;
    private String title;

    public B02_GetListReport() {
    }

    public B02_GetListReport(int reportId, int status, String code, String title) {
        this.reportId = reportId;
        this.status = status;
        this.code = code;
        this.title = title;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
