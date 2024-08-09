package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {

    AuthToken findOneById(int id);
    AuthToken findOneByJwtHash(String jwtHash);
    Optional<AuthToken> findByJwtHash(String jwtHash);
    @Modifying
    @Transactional
    void deleteOneById(int id);


}