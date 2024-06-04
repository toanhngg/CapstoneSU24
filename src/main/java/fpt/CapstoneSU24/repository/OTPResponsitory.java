package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.OTP;
import fpt.CapstoneSU24.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPResponsitory extends JpaRepository<OTP, Integer> {
    String findOTPByEmail(String mail);


}
