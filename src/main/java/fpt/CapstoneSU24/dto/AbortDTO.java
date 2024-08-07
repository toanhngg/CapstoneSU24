package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.CapstoneSU24.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbortDTO {


    @NotNull
    private String productRecognition;
    @NotNull
    private String email;
    @NotNull
    private String partyFullName;
    private LocationDTO location;

    private String description;

    @JsonIgnore
    private ImageItemLog imageItemLog;

    @NotNull
    private String OTP;
}
