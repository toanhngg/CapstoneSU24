package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.AuthTokens;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokensRepository extends JpaRepository<fpt.CapstoneSU24.model.AuthTokens, Integer> {

    AuthTokens findOneByUserAuth(User userAuth);

}