package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.Certificate;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Certificate findOneByCertificateId(int id);

    Certificate findOneByManufacturer_userId(int id);

    List<Certificate> findByManufacturer_userId(int id);
}