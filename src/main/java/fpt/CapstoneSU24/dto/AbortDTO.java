package fpt.CapstoneSU24.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.CapstoneSU24.model.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbortDTO {
    @JsonIgnore
    private int itemLogId;
    private Item item;
    private String address;
    private Party party;
    private Location location;
    private long timeStamp;
    private String description;
    private Authorized authorized;
    private EventType event_id;
    @JsonIgnore
    private int status;
    private ImageItemLog imageItemLog;
}
