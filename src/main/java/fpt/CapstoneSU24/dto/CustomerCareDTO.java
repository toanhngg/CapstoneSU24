package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCareDTO {
    @JsonIgnore
    private int careId;
    @NotNull(message = "CustomerName is not blank")
    private String customerName;
    @Email
    @NotNull(message = "Email is not blank")
    private String customerEmail;
    @NotNull(message = "customerPhone is not blank")
    private String customerPhone;
    @NotNull(message = "Content is not blank")
    @Size(min = 10,max = 500, message = "Content must be less than 255 characters")
    private String content;
    @JsonIgnore
    private long timestamp;
    @JsonIgnore
    private int status;
    @JsonIgnore
    private String note;
}
