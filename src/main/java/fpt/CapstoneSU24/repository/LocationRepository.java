package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

        @Query("SELECT l FROM Location l " +
                "LEFT JOIN ItemLog i ON i.location.locationId = l.locationId " +
                "WHERE i.itemLogId = :id")
        Location findOneByItemLogId(@Param("id") int id);

        @Modifying
        @Transactional
        @Query("UPDATE Location l SET l.address = :address, l.city = :city, l.country = :country, l.coordinateX = :coordinateX, l.coordinateY = :coordinateY, l.district = :district, l.ward = :ward WHERE l.locationId = :locationId")
        void updateLocation(@Param("locationId") int locationId,
                            @Param("address") String address,
                            @Param("city") String city,
                            @Param("country") String country,
                            @Param("coordinateX") double coordinateX,
                            @Param("coordinateY") double coordinateY,
                            @Param("district") String district,
                            @Param("ward") String ward);


}
