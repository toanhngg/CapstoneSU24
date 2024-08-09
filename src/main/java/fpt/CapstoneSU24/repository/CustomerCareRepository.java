package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.CustomerCare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface CustomerCareRepository extends JpaRepository<CustomerCare, Integer> {

//    @Query("SELECT c FROM CustomerCare c WHERE c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%")
//    List<CustomerCare> searchCustomerCare(@Param("keyword") String keyword);

    @Query("SELECT c FROM CustomerCare c WHERE (c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%) " +
            "OR (c.timestamp >= :startDate AND c.timestamp <= :endDate) OR c.status = :status ")
    List<CustomerCare> searchCustomerCare(@Param("keyword") String keyword, @Param("startDate") long startDate, @Param("endDate") long endDate,@Param("status") long status);

    @Query("SELECT c FROM CustomerCare c WHERE (c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%)")
    Page<CustomerCare> searchCustomerCare(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM CustomerCare c WHERE (c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%) AND c.timestamp >= :startDate AND c.timestamp <= :endDate")
    Page<CustomerCare> searchCustomerCareWithDate(@Param("keyword") String keyword, @Param("startDate") long startDate, @Param("endDate") long endDate, Pageable pageable);

    @Query("SELECT c FROM CustomerCare c WHERE (c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%) AND c.status = :status")
    Page<CustomerCare> searchCustomerCareWithStatus(@Param("keyword") String keyword, @Param("status") int status, Pageable pageable);

    @Query("SELECT c FROM CustomerCare c WHERE (c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%) AND c.timestamp >= :startDate AND c.timestamp <= :endDate AND c.status = :status")
    Page<CustomerCare> searchCustomerCareWithDateAndStatus(@Param("keyword") String keyword, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("status") int status, Pageable pageable);

    List<CustomerCare> findAllByStatus(int status);

}
