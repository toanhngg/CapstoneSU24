package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchSupportSystemByUserDTO {

    @Min(value = 0, message = "Status cannot be negative")
    private int status;

    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The pageNumber is required")
    private int pageNumber;

    @Min(value = 1, message = "Page size must be at least 1")
    private int pageSize;

    @NotNull(message = "Sort type cannot be null")
    private String type;
}
