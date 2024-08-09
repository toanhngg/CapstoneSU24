package fpt.CapstoneSU24.dto;

public class ItemViewDTOResponse {
    private int itemId;
    private String productRecognition;
    private long createdAt;
    private String statusEventType;
    private String address;
    private double coordinateX;
    private double coordinateY;

    public ItemViewDTOResponse(int itemId, String productRecognition, long createdAt, String statusEventType, String address, double coordinateX, double coordinateY) {
        this.itemId = itemId;
        this.productRecognition = productRecognition;
        this.createdAt = createdAt;
        this.statusEventType = statusEventType;
        this.address = address;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStatusEventType() {
        return statusEventType;
    }

    public void setStatusEventType(String statusEventType) {
        this.statusEventType = statusEventType;
    }
}
