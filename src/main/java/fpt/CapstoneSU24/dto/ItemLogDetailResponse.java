package fpt.CapstoneSU24.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemLogDetailResponse {

    private int itemLogId;
    private String eventType;
    private String partyFullname;
    private String sender;
    private String receiver;
    private String PartyPhoneNumber;
    private String addressInParty;
    private String coordinateX;
    private String coordinateY;
    private long timeReceive;
    private String descriptionItemLog;


}
