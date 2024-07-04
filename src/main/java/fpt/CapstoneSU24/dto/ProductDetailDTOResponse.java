package fpt.CapstoneSU24.dto;

import java.util.List;

public class ProductDetailDTOResponse {
    private int productId;
    private String productName;
    private String dimensions;
    private String material;
    private String description;
    private long createAt;
    private float weight;
    private int warranty;

    private String avatar;
    private List<String> listImages;

    public ProductDetailDTOResponse(int productId, String productName, String dimensions, String material, String description, long createAt, float weight, int warranty, String avatar, List<String> listImages) {
        this.productId = productId;
        this.productName = productName;
        this.dimensions = dimensions;
        this.material = material;
        this.description = description;
        this.createAt = createAt;
        this.weight = weight;
        this.warranty = warranty;
        this.avatar = avatar;
        this.listImages = listImages;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getListImages() {
        return listImages;
    }

    public void setListImages(List<String> listImages) {
        this.listImages = listImages;
    }

}