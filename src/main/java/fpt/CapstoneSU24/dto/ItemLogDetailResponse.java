package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.ItemLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemLogDetailResponse {

    private Integer itemLogId;
    private String eventType;
    private String partyFullname;
    private String sender;
    private String receiver;
    private String partyPhoneNumber;
    private String addressInParty;
    private Double coordinateX;
    private Double coordinateY;
    private Long timeReceive;
    private String descriptionItemLog;
    private Integer IdEdit;

    public ItemLogDetailResponse(Integer itemLogId, String eventType, String partyFullname, String sender, String receiver, String partyPhoneNumber, String addressInParty, Double coordinateX, Double coordinateY, Long timeReceive, String descriptionItemLog) {
        this.itemLogId = itemLogId;
        this.eventType = eventType;
        this.partyFullname = partyFullname;
        this.sender = sender;
        this.receiver = receiver;
        this.partyPhoneNumber = partyPhoneNumber;
        this.addressInParty = addressInParty;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.timeReceive = timeReceive;
        this.descriptionItemLog = descriptionItemLog;
    }
}
