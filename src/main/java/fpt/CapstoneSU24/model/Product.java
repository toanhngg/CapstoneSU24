package fpt.CapstoneSU24.model;


import jakarta.persistence.*;
import org.hibernate.Length;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    @Column(name = "product_name", columnDefinition = "nvarchar(255)")
    private String productName;
    //    người taọ ra sẳn phẩm này, người có role là manu
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "origin_id")
    private Origin origin;
    @ManyToOne
    @JoinColumn(name = "currentOwner_id")
    private User currentOwner;
    @Column(name = "unit_price")
    private String unitPrice;
    @Column(name = "created_at")
    private long createdAt;
    @Column(name = "dimensions")
    private String dimensions;
    @Column(name = "material")
    private String material;
    @Column(name = "supporting_documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    @Column(name = "product_recognition", columnDefinition = "nvarchar(255)")
    private String productRecognition;

    public Product(){

    }

    public Product(int productId, String productName, Category category, Origin origin, User currentOwner, String unitPrice, long createdAt, String dimensions, String material, String supportingDocuments, String productRecognition) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.origin = origin;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
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