package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.CapstoneSU24.model.Location;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditItemLogDTO {
    private String authorizedName;
    private String authorizedEmail;
    private LocationDTO location;
    private String description;
    @Email
    @NotNull(message = "Email is not blank")
    private String email;
    @NotNull(message = "ItemLogId is not blank")
    @Positive(message = "ItemLogId must be a positive number")
    private int itemLogId;
   private String phoneNumber;
    private String OTP;
}
