package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "party")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private int partyId;
    @Column(name = "party_full_name", columnDefinition = "nvarchar(50)")
    private String partyFullName;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "signature")
    private String signature;

    public Party(){

    }

    public Party(int partyId, String partyFullName, String description, String phoneNumber, String email, String signature) {
        this.partyId = partyId;
        this.partyFullName = partyFullName;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.signature = signature;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getPartyFullName() {
        return partyFullName;
    }

    public void setPartyFullName(String partyFullName) {
        this.partyFullName = partyFullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}