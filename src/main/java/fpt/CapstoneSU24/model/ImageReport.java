package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image_report") // Đặt tên bảng trong cơ sở dữ liệu
public class ImageReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "imagePath", columnDefinition = "nvarchar(255)")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public ImageReport() {
    }

    public ImageReport(String imageName, String imagePath, Report report) {
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.report = report;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
