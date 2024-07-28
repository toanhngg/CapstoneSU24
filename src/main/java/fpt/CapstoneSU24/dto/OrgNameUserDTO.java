package fpt.CapstoneSU24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrgNameUserDTO {
    private String orgName;
    private Integer  userId;
    private String userImage;

    public <T> OrgNameUserDTO(List<T> ts) {
    }
}
