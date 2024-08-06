package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    @Entity
    @Table(name = "authorized")
    public class Authorized {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "authorized_id")
        private int authorizedId;
        @Column(name = "authorized_name")
        private String authorizedName;
        @Column(name = "authorized_email")
        private String authorizedEmail;
        @Column(name = "assign_person")
        private String assignPerson;
        @Column(name = "assign_person_mail")
        private String assignPersonMail;
        @Column(name = "description", columnDefinition = "nvarchar(255)")
        private String description;
        @Column(name = "phone_number")
        private String phoneNumber;
}
