package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>  {
    public Product findOneByProductId(int id);
    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id")
    List<Product> findAllByManufacturerId(@Param("id")  int id);
    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND o.category.name LIKE :categoryName AND o.productName LIKE :productName")
    Page<Product> findAllProduct(@Param("id") int id, @Param("categoryName") String categoryName, @Param("productName") String productName, Pageable pageable);
    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND o.category.name LIKE :categoryName")
    List<Product> findAllProduct(@Param("id") int id, @Param("categoryName") String categoryName);
    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND o.category.name LIKE ':categoryName%' AND o.productName LIKE ':productName%' AND o.createAt <= :endDate AND o.createAt >= :startDate")
    Page<Product> findAllProductWithDate(@Param("id")  int id, @Param("categoryName") String categoryName, @Param("productName") String productName, @Param("startDate") long startDate, @Param("endDate") long endDate, Pageable pageable);
    Page<Product> findAllByProductNameContaining(String productName, Pageable pageable);
    Page<Product> findByManufacturerAndCreateAtBetweenAndProductNameContaining(User manufacturer, Long startDate, Long endDate, String productName, Pageable pageable);
    Page<Product> findByManufacturerAndCreateAtBetween(User manufacturer, Long startDate, Long endDate, Pageable pageable);
    Page<Product> findByManufacturerAndProductNameContaining(User manufacturer, String productName, Pageable pageable);
    @Modifying
    @Transactional
    void deleteOneByProductId(int productId);
    List<Product> findAllProductByCreateAtBetween(long startDate, long endDate);
    Product findOneByProductName(String productName);
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.categoryId = :categoryId")
    int countProductsByCategoryId(int categoryId);
}
