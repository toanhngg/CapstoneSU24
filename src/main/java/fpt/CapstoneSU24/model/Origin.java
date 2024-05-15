package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Origin") // Đặt tên bảng trong cơ sở dữ liệu
public class Origin {
    @Id
    @Column(name = "Id_Origin")
    private int idOrigin;

    @ManyToOne
    @JoinColumn(name = "Id_Product")
    private Product product;

    @Column(name = "Dimensions")
    private String dimensions;

    @Column(name = "Material")
    private String material;

    @Column(name = "Created_At")
    private String createdAt;

    @Column(name = "Updated_At")
    private String updatedAt;

    @Column(name = "End_At")
    private String endAt;

    @Column(name = "Supporting_Documents")
    private String supportingDocuments;

    @Column(name = "Acquisition_Method")
    private String acquisitionMethod;

    public Origin(){

    }
    public Origin(int idOrigin, Product product, String dimensions, String material, String createdAt, String updatedAt, String endAt, String supportingDocuments, String acquisitionMethod) {
        this.idOrigin = idOrigin;
        this.product = product;
        this.dimensions = dimensions;
        this.material = material;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.endAt = endAt;
        this.supportingDocuments = supportingDocuments;
        this.acquisitionMethod = acquisitionMethod;
    }

    public int getIdOrigin() {
        return idOrigin;
    }

    public void setIdOrigin(int idOrigin) {
        this.idOrigin = idOrigin;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public String getAcquisitionMethod() {
        return acquisitionMethod;
    }

    public void setAcquisitionMethod(String acquisitionMethod) {
        this.acquisitionMethod = acquisitionMethod;
    }
}
