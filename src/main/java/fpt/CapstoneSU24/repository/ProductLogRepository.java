package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductLogRepository extends JpaRepository<ProductLog, Integer> {
    @Override
    List<ProductLog> findAll();
//    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
//    List<Origin> findAllByProductId(@Param("id")  int id);

}