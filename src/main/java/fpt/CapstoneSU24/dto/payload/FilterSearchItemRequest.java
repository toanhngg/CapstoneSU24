package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class FilterSearchItemRequest {
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The productId is required")
    private Integer productId;
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageSize is required")
    private int pageSize;
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageNumber is required")
    private int pageNumber;
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "The startDate is required")
    private long startDate;
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "The endDate is required")
    private long endDate;
    @NotNull(message = "The name is not null")
    private String name;
    @NotNull(message = "The type is not null")
    private String type;
    @NotNull(message = "The productRecognition is not null")
    private String productRecognition;
    @NotNull(message = "The eventTypeId is not null")
    private Integer eventTypeId;

    public String getProductRecognition() {
        return productRecognition;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public Integer getProductId() {
        return productId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
