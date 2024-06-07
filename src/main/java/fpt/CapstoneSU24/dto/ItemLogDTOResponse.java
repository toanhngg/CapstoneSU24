package fpt.CapstoneSU24.dto;

import java.util.List;

public class ItemLogDTOResponse {
    private int ItemLogId;
    private String Address;
    private String CreatedAt;
    private String EventType;
    private String PartyName;
    private String Description;
    public ItemLogDTOResponse() {
    }

    public <T> ItemLogDTOResponse(List<T> ts) {
    }

    public ItemLogDTOResponse(String address, String createdAt, String eventType, String partyName, String description) {
        Address = address;
        CreatedAt = createdAt;
        EventType = eventType;
        PartyName = partyName;
        Description = description;
    }

    public int getItemLogId() {
        return ItemLogId;
    }

    public void setItemLogId(int itemLogId) {
        ItemLogId = itemLogId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
