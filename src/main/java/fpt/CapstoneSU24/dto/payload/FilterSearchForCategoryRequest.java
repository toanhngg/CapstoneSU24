package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class FilterSearchForCategoryRequest {
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageSize is required")
    private int pageSize;
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageNumber is required")
    private int pageNumber;
    @NotNull(message = "The name is not null")
    private String name;
    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }
    public String getName() {
        return name;
    }
}
