package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "origin") // Đặt tên bảng trong cơ sở dữ liệu
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "origin_id")
    private int originId;
    @Column(name = "created_at")
    private long createdAt;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "org_name", columnDefinition = "nvarchar(255)")
    private String org_name;
    @Column(name = "full_name_manufacturer", columnDefinition = "nvarchar(255)")
    private String fullNameManufacturer;
    @Column(name = "supporting_documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    public Origin(){
    }

   ;

    public Origin(int originId, long createdAt, String email, String phone, String org_name, String fullNameManufacturer,String description, Location location) {
        this.originId = originId;
        this.createdAt = createdAt;
        this.email = email;
        this.phone = phone;
        this.org_name = org_name;
        this.fullNameManufacturer = fullNameManufacturer;
      //  this.supportingDocuments = supportingDocuments;
        this.description = description;
        this.location = location;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullNameManufacturer() {
        return fullNameManufacturer;
    }

    public void setFullNameManufacturer(String fullNameManufacturer) {
        this.fullNameManufacturer = fullNameManufacturer;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }
}