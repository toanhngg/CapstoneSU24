package fpt.CapstoneSU24.dto;

import java.util.List;

public class ListCertificateDTOResponse {
    private int certId;
    private String name;
    private String issuingAuthority;
    private List<String> images;
    private long issuanceDate;
    private String note;

    public ListCertificateDTOResponse(int certId, String name, String issuingAuthority, List<String> images, long issuanceDate, String note) {
        this.certId = certId;
        this.name = name;
        this.issuingAuthority = issuingAuthority;
        this.images = images;
        this.issuanceDate = issuanceDate;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCertId() {
        return certId;
    }

    public void setCertId(int certId) {
        this.certId = certId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(long issuanceDate) {
        this.issuanceDate = issuanceDate;
    }
}
