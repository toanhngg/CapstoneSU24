package fpt.CapstoneSU24.model;

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
@Table(name = "customerCare")
public class CustomerCare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_id")
    private int careId;
    @Column(name = "customer_name", columnDefinition = "nvarchar(100)")
    private String customerName;
    @Column(name = "customer_email", columnDefinition = "nvarchar(100)")
    private String customerEmail;
    @Column(name = "customer_phone", columnDefinition = "nchar(11)")
    private String customerPhone;
    @Column(name = "content", columnDefinition = "nvarchar(300)")
    private String content;
    @Column(name = "timestamp", columnDefinition = "bigint")
    private long timestamp;
    @Column(name = "status", columnDefinition = "int")
    private int status;
    @Column(name = "note", columnDefinition = "nvarchar(300)")
    private String note;
}
