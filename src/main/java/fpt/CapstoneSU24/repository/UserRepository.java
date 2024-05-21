package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    List<User> findAll();
    public User findOneByUserId(int id);
    public User findOneByEmail(String email);
    public User findOneByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE (:email IS NULL OR u.email = :email) " +
            "AND (:roleId IS NULL OR u.role.id = :roleId) " +
            "AND (:isLock IS NULL OR u.isLock = :isLock) " +
            "AND (:dateFrom IS NULL OR u`.createOn >= :dateFrom) " +
            "AND (:dateTo IS NULL OR u.createOn <= :date`To)")
    Page<User> findByFilters(@Param("email") String email,
                             @Param("roleId") Integer roleId,
                             @Param("isLock") Boolean isLock,
                             @Param("dateFrom") long dateFrom,
                             @Param("dateTo") long dateTo,
                             Pageable pageable);
}

