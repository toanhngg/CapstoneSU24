package fpt.CapstoneSU24.dto;

public class ProductDTOResponse {
    private int productId;
    private String productName;
    private String description;
    private String avatar;

    public ProductDTOResponse(int productId, String productName, String description, String avatar) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.avatar = avatar;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
