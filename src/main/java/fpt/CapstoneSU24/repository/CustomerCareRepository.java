package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.CustomerCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface CustomerCareRepository extends JpaRepository<CustomerCare, Integer> {

//    @Query("SELECT c FROM CustomerCare c WHERE c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%")
//    List<CustomerCare> searchCustomerCare(@Param("keyword") String keyword);

    @Query("SELECT c FROM CustomerCare c WHERE (c.customerName LIKE %:keyword% OR c.customerPhone LIKE %:keyword% OR c.customerEmail LIKE %:keyword%) AND (c.timestamp >= :startDate AND c.timestamp <= :endDate)")
    List<CustomerCare> searchCustomerCare(@Param("keyword") String keyword, @Param("startDate") long startDate, @Param("endDate") long endDate);
}
