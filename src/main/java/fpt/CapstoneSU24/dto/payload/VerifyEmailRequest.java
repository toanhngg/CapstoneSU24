package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VerifyEmailRequest {
    @NotBlank(message = "The email is required")
    @Email(message = "The email is not a valid email")
    private String email;

    public String getEmail() {
        return email;
    }
}
