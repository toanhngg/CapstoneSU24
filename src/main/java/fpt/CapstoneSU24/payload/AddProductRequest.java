package fpt.CapstoneSU24.payload;

import jakarta.validation.constraints.*;

import java.util.List;

public class AddProductRequest {
    @NotBlank(message = "The productName is required")
    private String productName;
    @NotNull(message = "The certificateId is required")
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    private int categoryId;
    @NotBlank(message = "The dimensions is required")
    private String dimensions;
    @NotBlank(message = "The material is required")
    private String material;
    @NotNull(message = "The weight is required")
    @Min(value = 0, message = "Value must be positive")
    private float weight;
//    @NotBlank(message = "The unitPrice is required")
//    private String unitPrice;
    @NotBlank(message = "The description is required")
    private String description;
    @Min(value = 0, message = "Value must be positive")
    @NotNull(message = "The warranty is required")
    private int warranty;
//    @NotNull(message = "The certificateId is required")
//    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
//    private int certificateId;
    @NotEmpty(message = "The images is required")
    private List<String> images;
    @NotEmpty(message = "The images is required")
    private String avatar;
    @NotEmpty(message = "The images is required")
    private String file3D;

    public String getDimensions() {
        return dimensions;
    }

    public String getProductName() {
        return productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

//    public String getUnitPrice() {
//        return unitPrice;
//    }

    public String getMaterial() {
        return material;
    }

    public float getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public int getWarranty() {
        return warranty;
    }

//    public int getCertificateId() {
//        return certificateId;
//    }

    public List<String> getImages() {
        return images;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFile3D() {
        return file3D;
    }
}
