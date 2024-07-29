package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SendOTP {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
        private String email;
    @NotBlank(message = "OTP is required")
    private String otp;
     private LocationDTO location;

    }

