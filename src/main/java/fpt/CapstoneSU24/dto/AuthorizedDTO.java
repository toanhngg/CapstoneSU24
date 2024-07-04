package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.CapstoneSU24.model.Location;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorizedDTO {
    private int authorized_id;

    @NotBlank(message = "Authorized name is required")
    @Size(max = 20, message = "Authorized name must be less than 20 characters")
    private String authorized_name;

    @NotBlank(message = "Authorized email is required")
    @Email(message = "Authorized email should be valid")
    private String authorized_email;

    @NotBlank(message = "Assign person is required")
    @Size(max = 20, message = "Assign person must be less than 20 characters")
    private String assign_person;

    @NotBlank(message = "Assign person email is required")
    @Email(message = "Assign person email should be valid")
    private String assign_person_mail;

    @NotNull(message = "Location is required")
    private Location location;


   // @NotBlank(message = "Description is required")
    @Size(max = 100, message = "Description must be less than 500 characters")
    private String description;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number should be valid")
    private String phoneNumber;

    @NotBlank(message = "Product recognition is required")
    private String productRecognition;



    public AuthorizedDTO(int authorized_id, String authorized_name, String authorized_email, String assign_person, String assign_person_mail, Location location, String description, String phoneNumber, String productRecognition) {
        this.authorized_id = authorized_id;
        this.authorized_name = authorized_name;
        this.authorized_email = authorized_email;
        this.assign_person = assign_person;
        this.assign_person_mail = assign_person_mail;
        this.location = location;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.productRecognition = productRecognition;
    }

}
