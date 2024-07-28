package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCareDTO {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String content;
    @JsonIgnore
    private long timestamp;
    @JsonIgnore
    private int status;
}
