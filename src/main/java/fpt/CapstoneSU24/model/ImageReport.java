package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image_report") // Đặt tên bảng trong cơ sở dữ liệu
public class ImageReport {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image", columnDefinition = "varbinary(MAX)")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public ImageReport() {
    }

    public ImageReport(int id, String imageName, byte[] image, Report report) {
        this.id = id;
        this.imageName = imageName;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
