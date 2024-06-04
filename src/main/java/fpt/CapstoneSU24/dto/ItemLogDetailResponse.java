package fpt.CapstoneSU24.dto;

public class ItemLogDetailResponse {
private int itemLogId;
private String eventType;
private String partyFullname;
private String phoneNumber;
private String address;
private String coordinateX;
private String coordinateY;
private double timeReceive;
private String descriptionItemLog;


    public int getItemLogId() {
        return itemLogId;
    }

    public void setItemLogId(int itemLogId) {
        this.itemLogId = itemLogId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPartyFullname() {
        return partyFullname;
    }

    public void setPartyFullname(String partyFullname) {
        this.partyFullname = partyFullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
