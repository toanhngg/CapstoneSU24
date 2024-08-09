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
public class ItemLogDTOResponse {
    private int itemLogId;
    private String address;
    private String createdAt;
    private String eventType;
    private String partyName;
    private String description;
    private boolean checkPoint;
    public <T> ItemLogDTOResponse(List<T> ts) {
    }

}
