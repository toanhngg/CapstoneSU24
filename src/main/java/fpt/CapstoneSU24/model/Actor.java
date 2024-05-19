package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "actor")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private int actorId;
    @Column(name = "actor_name", columnDefinition = "nvarchar(50)")
    private String actorName;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "signature")
    private String signature;

    public Actor() {

    }

    public Actor(int actorId, String actorName, String description, String phoneNumber, String email, String signature) {
        this.actorId = actorId;
        this.actorName = actorName;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.signature = signature;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
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