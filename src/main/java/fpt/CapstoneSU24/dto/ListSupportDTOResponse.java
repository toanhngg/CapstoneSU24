package fpt.CapstoneSU24.dto;

import java.util.List;

public class ListSupportDTOResponse {
    private int supportSystemId;
    private String email;
    private String phoneNumber;
    private String title;
    private String status;
    private long timestamp;
    private String supporterName;
    private List<SubSupportDTOResponse> subSupport;

    public ListSupportDTOResponse(int supportSystemId, String email, String phoneNumber, String title, String status, long timestamp, String supporterName, List<SubSupportDTOResponse> subSupport) {
        this.supportSystemId = supportSystemId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.status = status;
        this.timestamp = timestamp;
        this.supporterName = supporterName;
        this.subSupport = subSupport;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSupportSystemId() {
        return supportSystemId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSupportSystemId(int supportSystemId) {
        this.supportSystemId = supportSystemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSupporterName() {
        return supporterName;
    }

    public void setSupporterName(String supporterName) {
        this.supporterName = supporterName;
    }

    public List<SubSupportDTOResponse> getSubSupport() {
        return subSupport;
    }

    public void setSubSupport(List<SubSupportDTOResponse> subSupport) {
        this.subSupport = subSupport;
    }
}
