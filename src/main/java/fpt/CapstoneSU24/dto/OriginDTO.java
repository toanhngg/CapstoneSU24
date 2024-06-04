package fpt.CapstoneSU24.dto;

public class OriginDTO {
    private String ProductName;
    private String ProductRecognition;
    private String OrgName;
    private String Phone;
    private String FullName;
    private long CreateAt;
    private String DescriptionProduct;
    private String DescriptionOrigin;
     private int Warranty;

     private byte[] image;

    public OriginDTO() {
    }

    public OriginDTO(String productName, String productRecognition, String orgName, String phone, String fullName, long createAt, String descriptionProduct, String descriptionOrigin, int warranty, byte[] image) {
        ProductName = productName;
        ProductRecognition = productRecognition;
        OrgName = orgName;
        Phone = phone;
        FullName = fullName;
        CreateAt = createAt;
        DescriptionProduct = descriptionProduct;
        DescriptionOrigin = descriptionOrigin;
        Warranty = warranty;
        this.image = image;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductRecognition() {
        return ProductRecognition;
    }

    public void setProductRecognition(String productRecognition) {
        ProductRecognition = productRecognition;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public long getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(long createAt) {
        CreateAt = createAt;
    }

    public String getDescriptionProduct() {
        return DescriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        DescriptionProduct = descriptionProduct;
    }

    public String getDescriptionOrigin() {
        return DescriptionOrigin;
    }

    public void setDescriptionOrigin(String descriptionOrigin) {
        DescriptionOrigin = descriptionOrigin;
    }

    public int getWarranty() {
        return Warranty;
    }

    public void setWarranty(int warranty) {
        Warranty = warranty;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
