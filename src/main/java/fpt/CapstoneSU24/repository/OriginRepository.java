package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OriginRepository extends JpaRepository<Origin, Integer> {
    @Override
    List<Origin> findAll();
    public Origin findOneByOriginId(int id);
    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
    List<Origin> findAllByProductId(@Param("id")  int id);

}
