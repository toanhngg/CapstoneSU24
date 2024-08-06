package fpt.CapstoneSU24.dto.ReportDTO;

import java.util.List;

public class CreateReportRequest {
    private String title;
    private int type;
    private int component;
    private int priority;
    private String causeDetail;
    private List<String> imageReports;
    private int createBy;
    private int  itemId;
    private int reportTo;

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

    public int getComponent() {
        return component;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCauseDetail() {
        return causeDetail;
    }

    public void setCauseDetail(String causeDetail) {
        this.causeDetail = causeDetail;
    }

    public List<String> getImageReports() {
        return imageReports;
    }

    public void setImageReports(List<String> imageReports) {
        this.imageReports = imageReports;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getReportTo() {
        return reportTo;
    }

    public void setReportTo(int reportTo) {
        this.reportTo = reportTo;
    }
}
