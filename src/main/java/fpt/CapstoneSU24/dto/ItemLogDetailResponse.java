package fpt.CapstoneSU24.dto;

public class ItemLogDetailResponse {
private int itemLogId;
private int eventType;
private String partyFullname;

private String sender;
private String receiver;
private String PartyPhoneNumber;
private String address;
private String coordinateX;
private String coordinateY;
private double timeReceive;
private String descriptionItemLog;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getItemLogId() {
        return itemLogId;
    }

    public void setItemLogId(int itemLogId) {
        this.itemLogId = itemLogId;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getPartyFullname() {
        return partyFullname;
    }

    public void setPartyFullname(String partyFullname) {
        this.partyFullname = partyFullname;
    }

    public String getPartyPhoneNumber() {
        return PartyPhoneNumber;
    }

    public void setPartyPhoneNumber(String partyPhoneNumber) {
        PartyPhoneNumber = partyPhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }

    public double getTimeReceive() {
        return timeReceive;
    }

    public void setTimeReceive(double timeReceive) {
        this.timeReceive = timeReceive;
    }

    public String getDescriptionItemLog() {
        return descriptionItemLog;
    }

    public void setDescriptionItemLog(String descriptionItemLog) {
        this.descriptionItemLog = descriptionItemLog;
    }
}
