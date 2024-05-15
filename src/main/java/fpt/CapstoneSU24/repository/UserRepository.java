package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    List<User> findAll();
    public User findOneByUserId(int id);
}

