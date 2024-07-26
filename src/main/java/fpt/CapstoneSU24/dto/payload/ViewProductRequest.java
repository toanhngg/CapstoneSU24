package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ViewProductRequest {
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The id is required")
    @Min(value = 1, message = "The id must be a positive number")
    private Integer id;
    @NotNull(message = "The category is required")
    private String category;
    public Integer getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}
