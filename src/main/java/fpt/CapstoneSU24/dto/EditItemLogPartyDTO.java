package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.Location;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditItemLogPartyDTO {
    private Location location;
    private String description;
    @Email
    @NotNull(message = "Email is not blank")
    private String email;
    @NotNull(message = "ItemLogId is not blank")
    @Positive(message = "ItemLogId must be a positive number")
    private int itemLogId;
    private String OTP;
}
