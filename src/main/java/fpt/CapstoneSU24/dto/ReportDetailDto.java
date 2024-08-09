package fpt.CapstoneSU24.dto;

import java.util.HashMap;
import java.util.Map;

public class ReportDetailDto {
    private int reportId;
    private String code;
    private String component;
    private String createBy;
    private String createOn;
    private int priority;
    private int status;
    private String title;
    private int type;
    private String updateOn;
    private String reportTo;
    private Map<String, String> reportImage = new HashMap<>();

    public ReportDetailDto() {
    }

    public ReportDetailDto(int reportId, String code, String component, String createBy, String createOn, int priority, int status, String title, int type, String updateOn, String reportTo, Map<String, String> reportImage) {
        this.reportId = reportId;
        this.code = code;
        this.component = component;
        this.createBy = createBy;
        this.createOn = createOn;
        this.priority = priority;
        this.status = status;
        this.title = title;
        this.type = type;
        this.updateOn = updateOn;
        this.reportTo = reportTo;
        this.reportImage = reportImage;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(String updateOn) {
        this.updateOn = updateOn;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    public Map<String, String> getReportImage() {
        return reportImage;
    }

    public void setReportImage(Map<String, String> reportImage) {
        this.reportImage = reportImage;
    }
}
