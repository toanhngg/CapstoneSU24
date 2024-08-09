package fpt.CapstoneSU24.dto;

import java.util.List;

public class ChangeStatusImageProduct {
    private List<String> productId;
    private String type;

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
