package fpt.CapstoneSU24.model;


import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Product")
    private int idProduct;

    @Column(name = "Product_Name", length = 10)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "Id_Manufacturer")
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "Id_Category")
    private Category category;

    @Column(name = "QuantityPerUnit", length = 10)
    private String quantityPerUnit;

    @Column(name = "UnitPrice", length = 10)
    private String unitPrice;

    @Column(name = "UnitsInStock", length = 10)
    private String unitsInStock;

    @Column(name = "UnitsOnOrder", length = 10)
    private String unitsOnOrder;

    @Column(name = "ReorderLevel", length = 10)
    private String reorderLevel;

    @Column(name = "Discontinued", length = 10)
    private String discontinued;
    @Column(name = "Supporting_Documents")
    private String supportingDocuments;
    public Product(){

    }
    public Product(int idProduct, String productName, Manufacturer manufacturer, Category category, String quantityPerUnit, String unitPrice, String unitsInStock, String unitsOnOrder, String reorderLevel, String discontinued, String supportingDocuments) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.category = category;
        this.quantityPerUnit = quantityPerUnit;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.unitsOnOrder = unitsOnOrder;
        this.reorderLevel = reorderLevel;
        this.discontinued = discontinued;
        this.supportingDocuments = supportingDocuments;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(String unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public String getUnitsOnOrder() {
        return unitsOnOrder;
    }

    public void setUnitsOnOrder(String unitsOnOrder) {
        this.unitsOnOrder = unitsOnOrder;
    }

    public String getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(String reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }
}