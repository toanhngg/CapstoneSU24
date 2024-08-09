package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository

public interface OTPRepository extends JpaRepository<OTP, Long> {

    @Query("SELECT o FROM OTP o WHERE o.email = :mail")
    OTP findOTPByEmail(String mail);


//        @Modifying
//        @Transactional
//        @Query("UPDATE OTP o SET o.codeOTP = :code WHERE o.email = :email")
//        void updateOtpByEmail(@Param("email") String email, @Param("code") String code);

    @Modifying
    @Transactional
    @Query("UPDATE OTP o SET o.codeOTP = :code, o.expiryTime = :expiryTime WHERE o.email = :email")
    void updateOtpByEmail(@Param("email") String email, @Param("code") String code, @Param("expiryTime") Date expiryTime);

}
