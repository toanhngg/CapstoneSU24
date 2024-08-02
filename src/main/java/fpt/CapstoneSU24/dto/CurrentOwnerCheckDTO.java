package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentOwnerCheckDTO {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(max = 10, message = "ProductRecognition must be less than 10 characters")
    @NotBlank(message = "ProductRecognition email is required")
    private String productRecognition;

    private String OTP;
}
