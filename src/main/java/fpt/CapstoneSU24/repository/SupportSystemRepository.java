package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.CustomerCare;
import fpt.CapstoneSU24.model.SupportSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportSystemRepository extends JpaRepository<SupportSystem, Integer> {

    //======================================= filter by admin
    @Query("SELECT c FROM SupportSystem c WHERE (c.user.email LIKE %:keyword% AND c.replyId = -1)")
    Page<SupportSystem> searchSupportSystem(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT c FROM SupportSystem c WHERE (c.user.email LIKE %:keyword%) AND c.timestamp >= :startDate AND c.timestamp <= :endDate AND c.replyId = -1")
    Page<SupportSystem> searchSupportSystemWithDate(@Param("keyword") String keyword, @Param("startDate") long startDate, @Param("endDate") long endDate, Pageable pageable);

    @Query("SELECT c FROM SupportSystem c WHERE (c.user.email LIKE %:keyword%) AND c.status = :status AND c.replyId = -1")
    Page<SupportSystem> searchSupportSystemWithStatus(@Param("keyword") String keyword, @Param("status") int status, Pageable pageable);

    @Query("SELECT c FROM SupportSystem c WHERE (c.user.email LIKE %:keyword%) AND c.timestamp >= :startDate AND c.timestamp <= :endDate AND c.status = :status AND c.replyId = -1")
    Page<SupportSystem> searchSupportSystemWithDateAndStatus(@Param("keyword") String keyword, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("status") int status, Pageable pageable);
    //=======================================

    //======================================= filter by user
    @Query("SELECT c FROM SupportSystem c WHERE (c.user.userId = :id AND c.replyId = -1)")
    Page<SupportSystem> searchSupportSystemByUser(@Param("id") int id, Pageable pageable);
    @Query("SELECT c FROM SupportSystem c WHERE (c.user.userId = :id AND c.status = :status AND c.replyId = -1)")
    Page<SupportSystem> searchSupportSystemByUser(@Param("id") int id, @Param("status") int status, Pageable pageable);
    //=======================================

    SupportSystem findOneBySupportSystemId(int supportSystemId);
    List<SupportSystem> findAllByReplyId(int replyId);
}
