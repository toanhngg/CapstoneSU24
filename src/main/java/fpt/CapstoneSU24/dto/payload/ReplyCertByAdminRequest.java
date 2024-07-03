package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ReplyCertByAdminRequest {
    @Getter
    @NotNull(message = "The manufacturerId is required")
    private Integer manufacturerId;
    @Getter
    @NotNull(message = "The isAccept is required")
    private Integer isAccept;
    @Getter
    @NotBlank(message = "The note is required")
    private String note;

}
