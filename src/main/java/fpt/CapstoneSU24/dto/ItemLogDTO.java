package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ItemLogDTO {
    @NotNull(message = "The quantity is required")
    @Min(value = 1, message = "The quantity must be at least 1")
    @Max(value = 1000, message = "The quantity must not exceed 1,000")
    private int quantity;
    private LocationDTO location;
    private String descriptionOrigin;

    @NotNull(message = "The productId is required")
    private int productId;


}
