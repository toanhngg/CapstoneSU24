package fpt.CapstoneSU24.dto;

import java.util.List;

public class RequestScanImageDTO {
    private String productId;
    private List<String> image;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
