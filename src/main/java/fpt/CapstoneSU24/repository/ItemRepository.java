package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Override
    Page<Item> findAll(Pageable pageable);

    @Query("SELECT o FROM Item o WHERE o.product.productId = :id")
    List<Item> findAllByProductId(@Param("id") int id);
//=====================================================================================
    @Query("SELECT i FROM Item i " +
            "JOIN ItemLog il ON i.itemId = il.item.itemId " +
            "WHERE il.itemLogId = (SELECT MAX(il2.itemLogId) FROM ItemLog il2 WHERE il2.item.itemId = i.itemId) " +
            "AND il.event_id.eventId = :eventType " +
            "AND i.product.productId = :id " +
            "AND i.currentOwner LIKE :currentOwner " +
            "AND i.productRecognition LIKE :productRecognition")
    Page<Item> findAllItem(
            @Param("id") int id,
            @Param("currentOwner") String currentOwner,
            @Param("productRecognition") String productRecognition,
            @Param("eventType") Integer eventType,
            Pageable pageable);
    @Query("SELECT o FROM Item o WHERE o.product.productId = :id " +
            "AND o.currentOwner LIKE :currentOwner " +
            "AND o.productRecognition LIKE :productRecognition")
    Page<Item> findAllItem(
            @Param("id") int id,
            @Param("currentOwner") String currentOwner,
            @Param("productRecognition") String productRecognition,
            Pageable pageable);
//=====================================================================================
    @Query("SELECT i FROM Item i " +
            "JOIN ItemLog il ON i.itemId = il.item.itemId " +
            "WHERE il.itemLogId = (SELECT MAX(il2.itemLogId) FROM ItemLog il2 WHERE il2.item.itemId = i.itemId) " +
            "AND il.event_id.eventId = :eventType " +
            "AND i.product.productId = :id " +
            "AND i.currentOwner LIKE :currentOwner " +
            "AND i.productRecognition LIKE :productRecognition " +
            "AND i.createdAt <= :endDate AND i.createdAt >= :startDate")
    Page<Item> findAllItemWithDate(
            @Param("id") int id,
            @Param("currentOwner") String currentOwner,
            @Param("productRecognition") String productRecognition,
            @Param("startDate") long startDate,
            @Param("endDate") long endDate,
            @Param("eventType") Integer eventType, Pageable pageable);
    @Query("SELECT o FROM Item o WHERE o.product.productId = :id " +
            "AND o.currentOwner LIKE :currentOwner " +
            "AND o.productRecognition LIKE :productRecognition " +
            "AND o.createdAt <= :endDate " +
            "AND o.createdAt >= :startDate")
    Page<Item> findAllItemWithDate(
            @Param("id") int id,
            @Param("currentOwner") String currentOwner,
            @Param("productRecognition") String productRecognition,
            @Param("startDate") long startDate,
            @Param("endDate") long endDate,
            Pageable pageable);




    List<Item> findByCreatedAtBetween(Long startDate, Long endDate);

    @Query("select i from Item i where i.product.productId = :id ")
    List<Item> findByProductId(@Param("id") Integer id);

    @Query("SELECT i FROM Item i WHERE i.productRecognition = :productRecognition")
    Item findByProductRecognition(@Param("productRecognition") String productRecognition);

    //DEFINED Status của bảng ITEM là 1 là đã có current_owner 0 là chưa có current_owner
    @Modifying
    @Transactional
    @Query("UPDATE Item i SET i.status = 0 , i.currentOwner = :currentOwner WHERE i.itemId = :itemId")
    void updateStatusAndCurrent(@Param("itemId") int itemId, @Param("currentOwner") String currentOwner);

    @Query("SELECT i FROM Item i " +
            "JOIN ItemLog il ON i.itemId = il.item.itemId " +
            "WHERE il.itemLogId = (SELECT MAX(il2.itemLogId) FROM ItemLog il2 WHERE il2.item.itemId = i.itemId) " +
            "AND il.event_id.eventId = :eventType")
    List<Item> getItemByEventType(@Param("eventType") int eventType);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Item i SET i.status = 1 WHERE i.itemId = :itemId")
//    void updateStatus(@Param("itemId") int itemId);

    // Phương thức cập nhật status của item

    @Modifying
    @Transactional
    @Query("UPDATE Item i SET i.status = :status WHERE i.productRecognition = :productRecognition")
    void updateItemStatus(@Param("productRecognition") String productRecognition, @Param("status") int status);

    @Modifying
    @Transactional
    @Query("UPDATE Item i SET i.status = :status , i.currentOwner = :currentOwner WHERE i.itemId = :itemId")
    void updateItemStatusAndCurrentOwnwe(@Param("itemId") Long itemId, @Param("status") int status, @Param("currentOwner") String currentOwner);


    @Query("SELECT i FROM Item i WHERE i.currentOwner = :currentOwner")
    List<Item> findByCurrentOwner(@Param("currentOwner") String currentOwner);
    List<Item> findAllItemByCreatedAtBetween(long startDate, long endDate);

}