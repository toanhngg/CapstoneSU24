package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class FilterSearchManufacturerRequest {
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageSize is required")
    private int pageSize;
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageNumber is required")
    private int pageNumber;
    @NotNull(message = "The orgName is not null")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "orgName must not contain special characters")
    private String orgName;
    @NotNull(message = "The type is not null")
    private String type;

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getType() {
        return type;
    }
}
