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
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private User manufacturer;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "unit_price")
    private String unitPrice;
    @Column(name = "dimensions")
    private String dimensions;
    @Column(name = "material", columnDefinition = "nvarchar(50)")
    private String material;
    @Column(name = "supporting_documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    @Column(name = "create_at")
    private long createAt;
    @Column(name = "weight")
    private float weight;
    @Column(name = "warranty")
    private int warranty;
    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    public Product(){}

    public Product(int productId, String productName, User manufacturer, Category category, String unitPrice, String dimensions, String material, String supportingDocuments, long createAt, float weight, int warranty, Certificate certificate) {
        this.productId = productId;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.category = category;
        this.unitPrice = unitPrice;
        this.dimensions = dimensions;
        this.material = material;
        this.supportingDocuments = supportingDocuments;
        this.createAt = createAt;
        this.weight = weight;
        this.warranty = warranty;
        this.certificate = certificate;
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
        return manufacturer;
    }

    public void setManufacturer(User manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }
}