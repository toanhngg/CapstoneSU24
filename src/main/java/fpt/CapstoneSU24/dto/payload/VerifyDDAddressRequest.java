package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VerifyDDAddressRequest {

    @NotNull(message = "The lat is not null")
    private Double lat;
    @NotNull(message = "The lng is not null")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
