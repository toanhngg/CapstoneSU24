package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ListManuToVerifyRequest {
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    @NotNull(message = "The phone is required")
    private String phone;
    @NotNull(message = "The type is not null")
    private String type;
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageSize is required")
    private int pageSize;
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageNumber is required")
    private int pageNumber;
    public String getPhone() {
        return phone;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getType() {
        return type;
    }
}
