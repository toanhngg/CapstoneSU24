package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventItemLogDTO {

   // //@NotBlank(message = "Address is mandatory")
//    private String address;
//
// // @NotBlank(message = "City is mandatory")
//    private String city;
//
// //@NotBlank(message = "Country is mandatory")
//    private String country;
//
//    private String district;
//
//    private String ward;
//
//
//    @DecimalMin(value = "-180.0", message = "CoordinateX must be between -180 and 180")
//    @DecimalMax(value = "180.0", message = "CoordinateX must be between -180 and 180")
//    private double coordinateX;
//
//    @DecimalMin(value = "-90.0", message = "CoordinateY must be between -90 and 90")
//    @DecimalMax(value = "90.0", message = "CoordinateY must be between -90 and 90")
//    private double coordinateY;
   private LocationDTO location;

    @Size(max = 255, message = "DescriptionItemLog must be less than 255 characters")
    private String descriptionItemLog;

    @JsonIgnore
    private int authorizedId;

    @NotBlank(message = "ProductRecognition is mandatory")
    private String productRecognition;

    @Positive(message = "EventId must be a positive number")
    private int eventId;

    @JsonIgnore
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "PhoneParty is invalid")
    private String phoneParty;

    @JsonIgnore
    @Size(max = 100, message = "FullNameParty must be less than 100 characters")
    private String fullNameParty;

    @JsonIgnore
    @Email(message = "EmailParty should be a valid email")
    private String emailParty;

    @JsonIgnore
    private byte[] imageItemLog;

    @Positive(message = "TransportId must be a positive number")
    private int transportId;

}
