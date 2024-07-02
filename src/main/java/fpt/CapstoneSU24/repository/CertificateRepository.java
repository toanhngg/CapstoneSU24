package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.Certificate;
import fpt.CapstoneSU24.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Certificate findOneByCertificateId(int id);

    Certificate findOneByManufacturer_userId(int id);

    List<Certificate> findByManufacturer_userId(int id);
    @Modifying
    @Transactional
    @Query("UPDATE Certificate c SET c.note = :note WHERE c.manufacturer.userId = :manufacturerId")
    int updateCertificateNoteByManufacturerId(@Param("manufacturerId") int manufacturerId, @Param("note") String note);
}