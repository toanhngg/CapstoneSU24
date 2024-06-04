package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "OTP")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private int otpId;
    @Column(name = "party_id")
    private int partyId;
    @Column(name = "email", columnDefinition = "nchar(100)")
    private String email;
    @Column(name = "codeOTP", columnDefinition = "nchar(10)")
    private String codeOTP;

    public OTP( String email, String codeOTP) {
        this.email = email;
        this.codeOTP = codeOTP;
    }
    public OTP() {
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodeOTP() {
        return codeOTP;
    }

    public void setCodeOTP(String codeOTP) {
        this.codeOTP = codeOTP;
    }
}
