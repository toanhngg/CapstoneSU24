package fpt.CapstoneSU24.dto;

import java.util.List;

public class ListSupportDTOResponse {
    private int supportSystemId;
    private String title;
    private String status;
    private long timestamp;
    private String supporterName;
    private List<SubSupportDTOResponse> subSupport;

    public ListSupportDTOResponse(int supportSystemId, String title, String status, long timestamp, String supporterName, List<SubSupportDTOResponse> subSupport) {
        this.supportSystemId = supportSystemId;
        this.title = title;
        this.status = status;
        this.timestamp = timestamp;
        this.supporterName = supporterName;
        this.subSupport = subSupport;
    }

    public int getSupportSystemId() {
        return supportSystemId;
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
