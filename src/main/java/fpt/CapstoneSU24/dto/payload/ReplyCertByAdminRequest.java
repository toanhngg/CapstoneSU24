package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotBlank;

public class ReplyCertByAdminRequest {
    @NotBlank(message = "The manufacturerId is required")
    private Integer manufacturerId;
    @NotBlank(message = "The isAccept is required")
    private Boolean isAccept;
    @NotBlank(message = "The note is required")
    private String note;

    public boolean isAccept() {
        return isAccept;
    }

    public String getNote() {
        return note;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }
}
