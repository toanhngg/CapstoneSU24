package fpt.CapstoneSU24.dto.ReportDTO;

import java.util.List;

public class ReportListDTO {
    private int id;
    private long createOn;
    private long updateOn;
    private String code;
    private String title;
    private int type;
    private int status;
    private int priority;
    private String createBy;
    private ReportTo reportTo;
    private int component;
    private String causeDetail;
    private String responseDetail;
    private List<ImageReport> imageReports;
    private String itemId;
    private String productName;


    public static class ReportTo {
        private String name;

        // Getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ImageReport {
        private int id;
        private String url;

        // Getters and setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    // Getters and setters for ReportListDTO

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateOn() {
        return createOn;
    }

    public void setCreateOn(long createOn) {
        this.createOn = createOn;
    }

    public long getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(long updateOn) {
        this.updateOn = updateOn;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ReportTo getReportTo() {
        return reportTo;
    }

    public void setReportTo(ReportTo reportTo) {
        this.reportTo = reportTo;
    }

    public int getComponent() {
        return component;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    public String getCauseDetail() {
        return causeDetail;
    }

    public void setCauseDetail(String causeDetail) {
        this.causeDetail = causeDetail;
    }

    public String getResponseDetail() {
        return responseDetail;
    }

    public void setResponseDetail(String responseDetail) {
        this.responseDetail = responseDetail;
    }

    public List<ImageReport> getImageReports() {
        return imageReports;
    }

    public void setImageReports(List<ImageReport> imageReports) {
        this.imageReports = imageReports;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
