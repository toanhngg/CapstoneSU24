package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.*;

import java.util.List;

public class ForgotPasswordRequest {
    @Email(message = "The email is not a valid email")
    @NotBlank(message = "The email is required")
    private String email;

    public String getEmail() {
        return email;
    }
}
