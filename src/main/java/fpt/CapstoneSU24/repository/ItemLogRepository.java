package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.ItemLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemLogRepository extends JpaRepository<ItemLog, Integer> {
    @Override
    List<ItemLog> findAll();
//    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
//    List<Origin> findAllByProductId(@Param("id")  int id);

}