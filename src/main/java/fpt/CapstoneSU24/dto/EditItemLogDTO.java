package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.Location;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditItemLogDTO {
    private Location location;
    private String description;
    @Email
    @NotNull(message = "Email is not blank")
    private String email;
    @NotNull(message = "ItemLogId is not blank")
    @Positive(message = "ItemLogId must be a positive number")
    private int itemLogId;
}
