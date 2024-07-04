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
    @JsonIgnore
    private Item item;
    private String productRecognition;
    private String email;
    @JsonIgnore
    private String address;
    @JsonIgnore
    private Party party;
    private Location location;
    @JsonIgnore
    private long timeStamp;
    private String description;
    @JsonIgnore
    private Authorized authorized;
    @JsonIgnore
    private EventType event_id;
    @JsonIgnore
    private int status;
    @JsonIgnore
    private ImageItemLog imageItemLog;
}
