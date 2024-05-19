package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "origin") // Đặt tên bảng trong cơ sở dữ liệu
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "origin_id")
    private int originId;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private User Manufacturer;
    @Column(name = "created_at")
    private long createdAt;
    @Column(name = "supporting_documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    public Origin(){
    }

    public Origin(int originId, User manufacturer, long createdAt, String supportingDocuments, String description) {
        this.originId = originId;
        Manufacturer = manufacturer;
        this.createdAt = createdAt;
        this.supportingDocuments = supportingDocuments;
        this.description = description;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public User getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(User manufacturer) {
        Manufacturer = manufacturer;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}