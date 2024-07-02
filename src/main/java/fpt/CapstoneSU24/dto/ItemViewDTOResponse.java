package fpt.CapstoneSU24.dto;

public class ItemViewDTOResponse {
    private int itemId;
    private String productRecognition;
    private long createdAt;
    private int status;

    public ItemViewDTOResponse(int itemId, String productRecognition, long createdAt, int status) {
        this.itemId = itemId;
        this.productRecognition = productRecognition;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getProductRecognition() {
        return productRecognition;
    }

    public void setProductRecognition(String productRecognition) {
        this.productRecognition = productRecognition;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
