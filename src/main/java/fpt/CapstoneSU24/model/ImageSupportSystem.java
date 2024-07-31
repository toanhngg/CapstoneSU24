package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image_support_system") // Đặt tên bảng trong cơ sở dữ liệu
public class ImageSupportSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_support_system_id")
    private int imageSupportSystemId;

    @Column(name = "file_path")
    private String filePath;
    @Column(name = "type", columnDefinition = "int")
    private int type;
    @ManyToOne
    @JoinColumn(name = "support_system_id")
    private SupportSystem supportSystem;

    public ImageSupportSystem(int imageSupportSystemId, String filePath, int type, SupportSystem supportSystem) {
        this.imageSupportSystemId = imageSupportSystemId;
        this.filePath = filePath;
        this.type = type;
        this.supportSystem = supportSystem;
    }

    public int getImageSupportSystemId() {
        return imageSupportSystemId;
    }

    public void setImageSupportSystemId(int imageSupportSystemId) {
        this.imageSupportSystemId = imageSupportSystemId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SupportSystem getSupportSystem() {
        return supportSystem;
    }

    public void setSupportSystem(SupportSystem supportSystem) {
        this.supportSystem = supportSystem;
    }
}

