package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Authorized;
import fpt.CapstoneSU24.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AuthorizedRepository extends JpaRepository<Authorized, Integer> {

    @Query("SELECT au FROM Authorized au " +
            "LEFT JOIN Location l ON au.location.locationId = l.locationId " +
            "WHERE au.authorizedId = :id")
    Authorized findAuthorizedById(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("UPDATE Authorized au SET au.authorizedEmail = :authorizedEmail, au.authorizedName = :authorizedName WHERE au.authorizedId = :authorizedId")
    void updateAuthorized(@Param("authorizedEmail") String authorizedEmail,
                        @Param("authorizedName") String authorizedName,
                        @Param("authorizedId") int authorizedId);

}
