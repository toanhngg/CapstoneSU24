package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    @NotNull(message = "The adress is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "The address must not contain special characters")
    @Size(min = 5, max = 200, message = "The address must be between 5 and 200 characters")
    private String address;
    @NotNull(message = "The city is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "The city must not contain special characters")
    @Size(min = 5, max = 200, message = "The city must be between 5 and 200 characters")
    private String city;
    @NotNull(message = "The country is required")
    @Size(min = 5, max = 200, message = "The country must be between 5 and 200 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "The country must not contain special characters")
    private String country;
    @NotNull(message = "The district is required")
    @Size(min = 5, max = 200, message = "The district must be between 5 and 200 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "The district must not contain special characters")
    private String district;
    @NotNull(message = "The ward is required")
    @Size(min = 5, max = 200, message = "The ward must be between 5 and 200 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{L}\\p{M}\\s.-]+$",
            message = "The ward must not contain special characters")
    private String ward;

    @NotNull(message = "The coordinateX is required")
    @DecimalMin(value = "-90.0", message = "Longitude must be greater than or equal to -90.0")
    @DecimalMax(value = "90.0", message = "Longitude must be less than or equal to 90.0")
    private double coordinateX;

    @NotNull(message = "The coordinateY is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.0")
    private double coordinateY;
}
