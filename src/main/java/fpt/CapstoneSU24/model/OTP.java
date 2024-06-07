package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "OTP")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private int otpId;
    @Column(name = "email", columnDefinition = "nchar(100)")
    private String email;
    @Column(name = "codeOTP", columnDefinition = "nchar(10)")
    private String codeOTP;
    @Column(name = "expiry_time")
    private Date expiryTime; // Thời gian hết hạn của OTP

    public OTP() {
    }

    public OTP(String email, String codeOTP, Date expiryTime) {
        this.email = email;
        this.codeOTP = codeOTP;
        this.expiryTime = expiryTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
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
