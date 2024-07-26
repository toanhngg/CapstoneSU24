package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventItemLogDTO {
  // private LocationDTO location;

    @Size(max = 255, message = "DescriptionItemLog must be less than 255 characters")
    private String descriptionItemLog;

    @JsonIgnore
    private int authorizedId;

    @NotBlank(message = "ProductRecognition is mandatory")
    private String productRecognition;

//    @Positive(message = "EventId must be a positive number")
//    private int eventId;

    @JsonIgnore
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "PhoneParty is invalid")
    private String phoneParty;

    @JsonIgnore
    @Size(max = 100, message = "FullNameParty must be less than 100 characters")
    private String fullNameParty;

    @Email(message = "EmailParty should be a valid email")
    private String emailParty;

    @JsonIgnore
    private byte[] imageItemLog;

    @Positive(message = "TransportId must be a positive number")
    private int transportId;

}
