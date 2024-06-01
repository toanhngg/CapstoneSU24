package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "[report]")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;
    @Column(name = "createOn")
    private long createOn;
    @Column(name = "updateOn")
    private long updateOn;
    @ManyToOne
    @JoinColumn(name = "update_by", nullable = true)
    private User updateBy;
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
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User createBy;
    @ManyToOne
    @JoinColumn(name = "report_to_id")
    private User reportTo;
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportComment> reportComments = new ArrayList<>();
    @Column(name = "component")
    private int component;
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageReport> imageReports = new ArrayList<>();

    public Report() {
    }

    public Report(int reportId, long createOn, long updateOn, User updateBy, String code, String title, int type, int status, int priority, User createBy, User reportTo, List<ReportComment> reportComments, int component, List<ImageReport> imageReports) {
        this.reportId = reportId;
        this.createOn = createOn;
        this.updateOn = updateOn;
        this.updateBy = updateBy;
        this.code = code;
        this.title = title;
        this.type = type;
        this.status = status;
        this.priority = priority;
        this.createBy = createBy;
        this.reportTo = reportTo;
        this.reportComments = reportComments;
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

    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
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

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getReportTo() {
        return reportTo;
    }

    public void setReportTo(User reportTo) {
        this.reportTo = reportTo;
    }

    public List<ReportComment> getReportComments() {
        return reportComments;
    }

    public void setReportComments(List<ReportComment> reportComments) {
        this.reportComments = reportComments;
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
