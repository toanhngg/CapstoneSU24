package fpt.CapstoneSU24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCareRponseDTO {
    public int careId;
    public String customerName;
    public String customerEmail;
    public String customerPhone;
    public String content;
    public long timestamp;
    public int status;
    public String note;
    public String username;
}
