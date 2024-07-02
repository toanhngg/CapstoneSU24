package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.*;

import java.util.List;

public class AddProductRequest {
    @NotBlank(message = "The productName is required")
    private String productName;
    @NotNull(message = "The categoryId is required")
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    private Integer categoryId;
    @NotBlank(message = "The dimensions is required")
    private String dimensions;
    @NotBlank(message = "The material is required")
    private String material;
    @NotNull(message = "The weight is required")
    @Min(value = 0, message = "Value must be positive")
    private Float weight;
//    @NotBlank(message = "The unitPrice is required")
//    private String unitPrice;
    @NotBlank(message = "The description is required")
    private String description;
    @Min(value = 0, message = "Value must be positive")
    @NotNull(message = "The warranty is required")
    private Integer warranty;
//    @NotNull(message = "The certificateId is required")
//    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
//    private int certificateId;
    @NotBlank(message = "The images is required")
    private List<String> images;
    @NotBlank(message = "The avatar is required")
    private String avatar;
    @NotNull(message = "The file3D is required")
    private String file3D;

    public String getProductName() {
        return productName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getMaterial() {
        return material;
    }

    public Float getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public Integer getWarranty() {
        return warranty;
    }

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
