package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "[report]")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;
    @Column(name = "createOn")
    protected long createOn;
    @Column(name = "updateOn")
    private long updateOn;
    @Column(name = "code", columnDefinition = "nvarchar(50)")
    private String code;
    @Column(name = "title", columnDefinition = "nvarchar(250)")
    private String title;
    @Column(name = "type")
    private int type;
    @Column(name = "status")
    private int status;
    @Column(name = "priority")
    private int priority;
    @Column(name = "createBy", columnDefinition = "nvarchar(250)")
    private String createBy;
    @ManyToOne
    @JoinColumn(name = "report_to_id")
    private User reportTo;
    @Column(name = "component")
    private int component;
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageReport> imageReports = new ArrayList<>();

    public Report() {
    }

    public Report(int reportId, long createOn, long updateOn, String code, String title, int type, int status, int priority, String createBy, User reportTo, int component, List<ImageReport> imageReports) {
        this.reportId = reportId;
        this.createOn = createOn;
        this.updateOn = updateOn;
        this.code = code;
        this.title = title;
        this.type = type;
        this.status = status;
        this.priority = priority;
        this.createBy = createBy;
        this.reportTo = reportTo;
        this.component = component;
        this.imageReports = imageReports;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
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

    public User getReportTo() {
        return reportTo;
    }

    public void setReportTo(User reportTo) {
        this.reportTo = reportTo;
    }

    public int getComponent() {
        return component;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    public List<ImageReport> getImageReports() {
        return imageReports;
    }

    public void setImageReports(List<ImageReport> imageReports) {
        this.imageReports = imageReports;
    }
}
