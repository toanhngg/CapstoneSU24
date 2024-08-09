package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotNull;

public class ProductNameRequest {
    @NotNull(message = "The productName is required")
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
