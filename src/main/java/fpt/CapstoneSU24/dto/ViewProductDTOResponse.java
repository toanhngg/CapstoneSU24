package fpt.CapstoneSU24.dto;

public class ViewProductDTOResponse {
    private int productId;
    private String productName;
    private String description;
    private String avatar;
    private String dimensions;
    private String material;
    private float weight;

    public ViewProductDTOResponse(int productId, String productName, String description, String avatar, String dimensions, String material, float weight) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.avatar = avatar;
        this.dimensions = dimensions;
        this.material = material;
        this.weight = weight;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
