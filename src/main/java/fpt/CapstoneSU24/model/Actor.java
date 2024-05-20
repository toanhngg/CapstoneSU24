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

}