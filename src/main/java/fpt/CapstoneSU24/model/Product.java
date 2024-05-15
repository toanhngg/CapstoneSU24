package fpt.CapstoneSU24.model;


import jakarta.persistence.*;
import org.hibernate.Length;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Product")
    private int productId;
    @Column(name = "Product_Name", columnDefinition = "nvarchar(255)")
    private String productName;
    //    người taọ ra sẳn phẩm này, người có role là manu
    @ManyToOne
    @JoinColumn(name = "Id_Manufacturer")
    private User Manufacturer;
    @ManyToOne
    @JoinColumn(name = "Id_Category")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "Id_Current_Owner")
    private User currentOwner;
    @Column(name = "Unit_Price")
    private String unitPrice;
    @Column(name = "Created_At")
    private String createdAt;
    @Column(name = "Dimensions")
    private String dimensions;
    @Column(name = "Material", columnDefinition = "nvarchar(50)")
    private String material;
    @Column(name = "Supporting_Documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    //    có thể là đường dẫn đến file ảnh, file model 3d, QR...
    @Column(name = "Product_Recognition", columnDefinition = "nvarchar(255)")
    private String productRecognition;

    public Product(){

    }

    public Product(int productId, String productName, User manufacturer, Category category, User currentOwner, String unitPrice, String createdAt, String dimensions, String material, String supportingDocuments, String productRecognition) {
        this.productId = productId;
        this.productName = productName;
        Manufacturer = manufacturer;
        this.category = category;
        this.currentOwner = currentOwner;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
        this.dimensions = dimensions;
        this.material = material;
        this.supportingDocuments = supportingDocuments;
        this.productRecognition = productRecognition;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public User getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(User manufacturer) {
        Manufacturer = manufacturer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(User currentOwner) {
        this.currentOwner = currentOwner;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public String getProductRecognition() {
        return productRecognition;
    }

    public void setProductRecognition(String productRecognition) {
        this.productRecognition = productRecognition;
    }
}