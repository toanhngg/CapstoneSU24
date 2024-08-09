package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FilterCustomerCareRequest {
    @NotNull(message = "The keyword is required")
    private String keyword;
    @NotNull(message = "The type is required")
    private String type;
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "The startDate is required")
    private long startDate;
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "The endDate is required")
    private long endDate;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
