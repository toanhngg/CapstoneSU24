package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
    @Entity
    @Table(name = "authorized")
    public class Authorized {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "authorized_id")
        private int authorized_id;
        @Column(name = "authorized_name")
        private String authorized_name;
        @Column(name = "authorized_email")
        private String authorized_email;
        @Column(name = "assign_person")
        private String assign_person;
        @Column(name = "assign_person_mail")
        private String assign_person_mail;
        @ManyToOne
        @JoinColumn(name = "location_id")
        private Location location;
        @Column(name = "description", columnDefinition = "nvarchar(255)")
        private String description;
        @Column(name = "phone_number")
        private String phoneNumber;


    public Authorized() {
    }

    public Authorized(String authorized_name, String authorized_email, String assign_person, String assign_person_mail, Location location, String description, String phoneNumber) {
            this.authorized_name = authorized_name;
            this.authorized_email = authorized_email;
            this.assign_person = assign_person;
            this.assign_person_mail = assign_person_mail;
            this.location = location;
            this.description = description;
            this.phoneNumber = phoneNumber;
        }

        public Authorized(String authorized_name, String authorized_email, Location location, String description, String phoneNumber) {
            this.authorized_name = authorized_name;
            this.authorized_email = authorized_email;
            this.location = location;
            this.description = description;
            this.phoneNumber = phoneNumber;
        }
    }
