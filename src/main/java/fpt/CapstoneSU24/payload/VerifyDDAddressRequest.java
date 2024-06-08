package fpt.CapstoneSU24.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VerifyDDAddressRequest {

    @NotNull(message = "The lat is not null")
    private double lat;
    @NotNull(message = "The lng is not null")
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
