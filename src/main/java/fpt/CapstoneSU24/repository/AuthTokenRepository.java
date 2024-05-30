package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {

    AuthToken findOneById(int id);
    AuthToken findOneByJwtHash(String jwtHash);

}