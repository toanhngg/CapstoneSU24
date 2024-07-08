package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.CapstoneSU24.model.Location;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthorizedDTO {
    private int authorizedId;

    @NotBlank(message = "Authorized name is required")
    @Size(max = 5, message = "Authorized name must be less than 5 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "Authorized name must not contain special characters")
    private String authorizedName;

    @NotBlank(message = "Authorized email is required")
    @Email(message = "Authorized email should be valid")
    private String authorizedEmail;

    @NotBlank(message = "Assign person is required")
    @Size(max = 5, message = "Assign person must be less than 5 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "AAssign person must not contain special characters")
    private String assignPerson;

    @NotBlank(message = "Assign person email is required")
    @Email(message = "Assign person email should be valid")
    private String assignPersonMail;

    private LocationDTO location;

    // @NotBlank(message = "Description is required")
    @Size(max = 50, message = "Description must be less than 50 characters")
    private String description;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number should be valid")
    private String phoneNumber;

    @NotBlank(message = "Product recognition is required")
    @Size(max = 10, message = "ProductRecognition must be 10 characters")
    private String productRecognition;





}
