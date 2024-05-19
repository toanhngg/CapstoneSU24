package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>  {
    public Product findOneByProductId(int id);
//    @Query("SELECT o FROM Product o WHERE o.Manufacturer.userId = :id")
//    List<Product> findAllByManufacturerId(@Param("id")  int id);

}
