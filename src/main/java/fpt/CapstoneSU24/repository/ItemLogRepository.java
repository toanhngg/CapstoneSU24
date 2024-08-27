package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.dto.ItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface  ItemLogRepository extends JpaRepository<ItemLog, Integer> {
    @Override
    List<ItemLog> findAll();

    @Query("SELECT il " +
            "FROM ItemLog il " +
            "LEFT JOIN Party p ON p.partyId = il.party.partyId " +
            "LEFT JOIN Location l ON il.location.locationId = l.locationId " +
            "WHERE il.itemLogId = :itemLogId")
    ItemLog getItemLogsById(@Param("itemLogId") int itemLogId);

    @Query("SELECT il " +
            "FROM ItemLog il " +
            "LEFT JOIN Item i ON il.item.itemId = i.itemId " +
            "LEFT JOIN Origin o ON i.origin.originId = o.originId " +
            "LEFT JOIN Product p ON i.product.productId = p.productId " +
            "LEFT JOIN ImageProduct ipr ON p.productId = ipr.product.productId " +
            "LEFT JOIN Category c ON p.category.categoryId = c.categoryId " +
            "LEFT JOIN Location l ON o.location.locationId = l.locationId " +
            "WHERE il.itemLogId = :itemLogId")
    ItemLog getItemLogs(@Param("itemLogId") int itemLogId);

    @Query("SELECT il FROM ItemLog il LEFT JOIN  il.location loc LEFT JOIN il.item i WHERE i.itemId = :itemId AND il.event_id.eventId <> 6 ORDER BY il.itemLogId desc")
    List<ItemLog> getItemLogsByItemId(@Param("itemId") int itemId);

    @Query("SELECT il FROM ItemLog il LEFT JOIN  il.location loc LEFT JOIN il.item i WHERE i.itemId = :itemId AND il.event_id.eventId <> 6 ORDER BY il.itemLogId desc")
    List<ItemLog> getItemLogsByItemIdIgnoreEdit(@Param("itemId") int itemId);


//    @Query("SELECT il FROM Item i JOIN ItemLog il ON i.itemId = il.item.itemId " +
//            "WHERE il.itemLogId = (SELECT MAX(il2.itemLogId) FROM ItemLog il2 WHERE il2.item.itemId = i.itemId AND il2.event_id.eventId <> 6)")
//    Optional<ItemLog> findFirstByItem_ItemIdOrderByItemLogIdDesc(int itemId);

    @Query("SELECT il FROM ItemLog il " +
            "JOIN il.item i " +
            "WHERE il.itemLogId = (SELECT MAX(il2.itemLogId) FROM ItemLog il2 " +
            "WHERE il2.item.itemId = i.itemId AND il2.event_id.eventId <> 6) " +
            "AND il.item.itemId = :itemId " +
            "AND il.event_id.eventId <> 6")
    Optional<ItemLog> findFirstByItem_ItemIdOrderByItemLogIdDesc(@Param("itemId") int itemId);

//    @Query("SELECT il FROM ItemLog il LEFT JOIN  il.location loc LEFT JOIN il.item i WHERE i.itemId = :itemId ORDER BY il.itemLogId asc")
//    List<ItemLog> getItemLogsByItemIdAsc(@Param("itemId") int itemId);


//    @Query("SELECT il FROM ItemLog il LEFT JOIN  il.location loc LEFT JOIN il.item i LEFT JOIN il.party p WHERE il.itemLogId = :idEdit ORDER BY il.itemLogId asc")
//    List<ItemLog> getListItemLogsByIdEdit(@Param("idEdit") int idEdit);

    @Query("SELECT new fpt.CapstoneSU24.dto.ItemLogDetailResponse(" +
            "il.itemLogId, " +
            "il.event_id.event_type, " +
            "p.partyFullName, " +
            "a.assignPersonMail, " +
            "a.authorizedEmail, " +
            "p.phoneNumber, " +
            "il.address, " +
            "loc.coordinateX, " +
            "loc.coordinateY, " +
            "il.timeStamp, " +
            "il.description) " +
            "FROM ItemLog il " +
            "LEFT JOIN il.location loc " +
            "LEFT JOIN il.party p " +
            "LEFT JOIN il.authorized a " +
            "WHERE il.idEdit = :itemLogId " +
            "ORDER BY il.itemLogId ASC")
    List<ItemLogDetailResponse> getListItemLogsByIdEdit(@Param("itemLogId") int itemLogId);

    @Query("SELECT il FROM ItemLog il LEFT JOIN  il.location loc LEFT JOIN il.item i WHERE i.itemId = :itemId AND il.event_id.eventId <> 6 ORDER BY il.itemLogId asc")
    List<ItemLog> getItemLogsByItemIdAscNotEdit(@Param("itemId") int itemId);

    @Query("SELECT il FROM ItemLog il LEFT JOIN  il.location loc LEFT JOIN il.item i WHERE i.itemId = :itemId AND il.event_id.eventId <> 6 ORDER BY il.itemLogId desc")
    List<ItemLog> getItemLogsByItemIdDescNotEdit(@Param("itemId") int itemId);

    @Query("SELECT il FROM ItemLog il LEFT JOIN il.item i WHERE i.itemId = :itemId AND il.point IS NOT NULL")
    List<ItemLog> getPointItemId(@Param("itemId") Integer itemId);

    @Query("SELECT il FROM ItemLog il LEFT JOIN il.item i WHERE i.itemId = :itemId AND il.event_id.eventId <> 6  AND il.point IS NOT NULL ORDER BY il.itemLogId desc ")
    List<ItemLog> getPointItemIdIgnoreEdit(@Param("itemId") Integer itemId);

    @Query("SELECT count(il) FROM ItemLog il WHERE il.point IS NOT NULL  AND il.event_id.eventId <> 6 ")
    Integer countPoint();

    @Query("SELECT count(il) FROM ItemLog il  WHERE il.event_id.eventId <> 6  ")
    Integer countItemId();

//    @Modifying
//    @Transactional
//    @Query("UPDATE ItemLog i SET i.authorized.authorizedId = :authorizedId  WHERE i.itemLogId = :itemLogId")
//    void updateAuthorized(@Param("authorizedId") int authorizedId, @Param("itemLogId") int itemLogId);

    @Modifying
    @Transactional
    @Query("UPDATE ItemLog i SET i.status = :status  WHERE i.itemLogId = :itemLogId")
    void updateStatus(@Param("status") int status, @Param("itemLogId") int itemLogId);

    @Modifying
    @Transactional
    @Query("UPDATE ItemLog i SET i.address = :address, i.location.locationId = :locationId, " +
            "i.point = :point, i.description = :description, i.idEdit = :idItem WHERE i.itemLogId = :itemLogId")
    void updateItemLogByParty(@Param("address") String address,
                              @Param("locationId") int locationId,
                              @Param("point") String point,
                              @Param("itemLogId") int itemLogId,
                              @Param("description") String description,
                              @Param("idItem") int idItem);

    @Modifying
    @Transactional
    @Query("UPDATE ItemLog i SET i.address = :address,i.location.locationId = :locationId," +
            "i.authorized.authorizedId = :authorizedId ,i.point =:point,i.description =:description,i.idEdit = :idItem WHERE i.itemLogId = :itemLogId")
    void updateItemLog(@Param("address") String address,@Param("locationId") int locationId,@Param("authorizedId") int authorizedId,
                       @Param("point") String point, @Param("itemLogId") int itemLogId,
                       @Param("description") String description,@Param("idItem") int idItem);
    @Modifying
    @Transactional
    @Query("UPDATE ItemLog i SET i.point = :point, i.description = :description, i.idEdit = :idItem, i.party.partyId = :party WHERE i.itemLogId = :itemLogId")
    void updateItemLogTransport(@Param("point") String point,
                                @Param("description") String description,
                                @Param("idItem") int idItem,
                                @Param("party") Integer party,
                                @Param("itemLogId") long itemLogId);


    @Query("SELECT il FROM ItemLog il LEFT JOIN il.party p LEFT JOIN il.item i WHERE il.event_id.eventId <> 6 AND  i.itemId = :itemId " +
            "AND p.email = :email")
    List<ItemLog> checkParty(int itemId, String email);

    @Modifying
    @Transactional
    @Query("UPDATE ItemLog i SET i.location.locationId = :location, i.point = :point, i.address = :address WHERE  i.itemLogId = :itemLogId")
    void updateItemLogLocation(@Param("location") int location,
                               @Param("point") String point,
                               @Param("address") String address,
                               @Param("itemLogId") int itemLogId);
    @Procedure(name = "InsertItemLog")
    void insertItemLog(
            String address,
            String description,
            Integer status,
            Long timestamp,
            Integer authorizedId,
            Integer eventId,
            Integer itemId,
            Integer locationId,
            Integer partyId,
            String point,
            Integer idEdit
    );
}