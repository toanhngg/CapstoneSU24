package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificateId;
    @Column(name = "certificate_name", columnDefinition = "nvarchar(50)")
    private String certificateName;
    @Column(name = "issuing_authority", columnDefinition = "nvarchar(50)")
    private String issuingAuthority;
    @Column(name = "image", columnDefinition = "VARBINARY(MAX)")
    private byte[] image;
    @Column(name = "issuance_date")
    private long issuanceDate;

    public Certificate(int certificateId, String certificateName, String issuingAuthority, byte[] image, long issuanceDate) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.issuingAuthority = issuingAuthority;
        this.image = image;
        this.issuanceDate = issuanceDate;
    }

    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(long issuanceDate) {
        this.issuanceDate = issuanceDate;
    }
}
