package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyAddressRequest {
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s,]+$",
            message = "Address must not contain special characters except for spaces and commas")
    @NotBlank(message = "The address is required")
    private String address;

    public String getAddress() {
        return address;
    }
}
