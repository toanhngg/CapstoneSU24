package fpt.CapstoneSU24.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int MemberId;
    @Nationalized
    String Email;
    String CompanyName;
    String City;
    String Country;
    String Password;
}
