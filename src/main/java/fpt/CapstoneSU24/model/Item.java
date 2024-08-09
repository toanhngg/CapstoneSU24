package fpt.CapstoneSU24.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item") // Đặt tên bảng trong cơ sở dữ liệu
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "currentOwner")
    private String currentOwner;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "origin_id")
    private Origin origin;
    @Column(name = "created_at")
    private long createdAt;
    @Column(name = "product_recognition", columnDefinition = "nvarchar(255)")
    private String productRecognition;
    @Column(name = "status")
    private int status;
    @Column(name = "certificate_link", columnDefinition = "varchar(MAX)")
    private String certificateLink;
}
