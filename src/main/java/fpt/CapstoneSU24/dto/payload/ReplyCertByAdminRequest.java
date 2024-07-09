package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.*;
import lombok.Getter;

public class ReplyCertByAdminRequest {
    @Getter
    @Min(value = 1, message = "The id must be a positive number")
    @NotNull(message = "The manufacturerId is required")
    private Integer manufacturerId;
    @Getter
    @NotNull(message = "The isAccept is required")
    @Min(value = 0, message = "isAccept must be 0 or 1")
    @Max(value = 1, message = "isAccept must be 0 or 1")
    private Integer isAccept;
    @Getter
    @NotBlank(message = "The note is required")
    private String note;

}
