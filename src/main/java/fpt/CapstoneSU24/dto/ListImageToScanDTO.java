package fpt.CapstoneSU24.dto;

import java.util.List;

public class ListImageToScanDTO {
    private String productId;
    private String productName;
    private String manufactorName;
    private Long requestDate;
    private String status;
    private List<String> filePath;

    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

    public String getManufactorName() {
        return manufactorName;
    }

    public Long getRequestDate() {
        return requestDate;
    }



    public ListImageToScanDTO() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufactorName(String email) {
        return manufactorName;
    }

    public void setManufactorName(String manufactorName) {
        this.manufactorName = manufactorName;
    }

    public Long getRequestDate(Long requestScanDate) {
        return requestDate;
    }

    public void setRequestDate(Long requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
