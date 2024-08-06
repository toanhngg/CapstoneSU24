package fpt.CapstoneSU24.dto;

import java.util.List;

public class SubSupportDTOResponse {
    private int supportSystemId;
    private String content;
    private List<String> images;
    private long timestamp;
    private String supportContent;
    private List<String> SupportImage;
    private long supportTimestamp;

    public SubSupportDTOResponse(int supportSystemId, String content, List<String> images, long timestamp, String supportContent, List<String> supportImage, long supportTimestamp) {
        this.supportSystemId = supportSystemId;
        this.content = content;
        this.images = images;
        this.timestamp = timestamp;
        this.supportContent = supportContent;
        SupportImage = supportImage;
        this.supportTimestamp = supportTimestamp;
    }

    public int getSupportSystemId() {
        return supportSystemId;
    }

    public void setSupportSystemId(int supportSystemId) {
        this.supportSystemId = supportSystemId;
    }

    public String getSupportContent() {
        return supportContent;
    }

    public void setSupportContent(String supportContent) {
        this.supportContent = supportContent;
    }

    public List<String> getSupportImage() {
        return SupportImage;
    }

    public void setSupportImage(List<String> supportImage) {
        SupportImage = supportImage;
    }

    public long getSupportTimestamp() {
        return supportTimestamp;
    }

    public void setSupportTimestamp(long supportTimestamp) {
        this.supportTimestamp = supportTimestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
