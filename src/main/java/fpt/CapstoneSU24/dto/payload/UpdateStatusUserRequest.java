package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class UpdateStatusUserRequest {
    @NotNull(message = "The id is required")
    private Integer id;
    @NotNull(message = "The status is required")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }
}
