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
}
