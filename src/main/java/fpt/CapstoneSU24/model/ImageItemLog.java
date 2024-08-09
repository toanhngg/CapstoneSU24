package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image_item_log") // Đặt tên bảng trong cơ sở dữ liệu
public class ImageItemLog {
    @Id
    @Column(name = "item_log_id")
    private int id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image", columnDefinition = "varbinary(MAX)")
    private byte[] image;
    @OneToOne
    @MapsId
    @JoinColumn(name = "item_log_id")
    private ItemLog itemLog;
}
