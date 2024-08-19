package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.dto.ProductResponseCustomDTO;
import fpt.CapstoneSU24.model.Item;
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

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product findOneByProductId(int id);

    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id")
    List<Product> findAllByManufacturerId(@Param("id") int id);

    @Query("SELECT o FROM Product o " +
            "WHERE o.manufacturer.userId = :id " +
            "AND (:categoryName IS NULL OR :categoryName = '' OR o.category.name LIKE CONCAT(:categoryName, '%')) " +
            "AND (:productName IS NULL OR :productName = '' OR o.productName LIKE CONCAT(:productName, '%')) ")
    Page<Product> findAllProduct(
            @Param("id") int id,
            @Param("categoryName") String categoryName,
            @Param("productName") String productName,
            Pageable pageable);

    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND o.category.categoryId = :categoryId")
    List<Product> findAllProduct(@Param("id") int id, @Param("categoryId") int categoryId);

    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id")
    List<Product> findAllProduct(@Param("id") int id);

    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND " +
            "o.createAt <= :endDate AND o.createAt >= :startDate")
    Page<Product> findAllProductWithDate(
            @Param("id") int id,
            @Param("startDate") Long startDate,
            @Param("endDate") Long endDate,
            Pageable pageable);


//    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND o.category.name LIKE ':categoryName%' AND o.productName LIKE ':productName%' AND o.createAt <= :endDate AND o.createAt >= :startDate")
//    Page<Product> findAllProductWithDateAndKeyword(@Param("id") int id, @Param("categoryName") String categoryName, @Param("productName") String productName, @Param("startDate") long startDate, @Param("endDate") long endDate, Pageable pageable);
    @Query("SELECT o FROM Product o WHERE o.manufacturer.userId = :id AND o.category.name LIKE CONCAT(:categoryName, '%') AND o.productName LIKE CONCAT(:productName, '%') AND o.createAt BETWEEN :startDate AND :endDate")
   Page<Product> findAllProductWithDateAndKeyword(@Param("id") int id, @Param("categoryName") String categoryName, @Param("productName") String productName, @Param("startDate") Long startDate, @Param("endDate") Long endDate, Pageable pageable);

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

    @Query("SELECT p FROM Product p " +
            "JOIN p.imageProducts ip " +
            "WHERE ip.type = 1 " +
            "AND (:productName = '' OR p.productName LIKE %:productName%) " +
            "AND (:productId < 1 OR p.productId = :productId) " +
            "AND (:manufactorName = '' OR p.manufacturer.email LIKE %:manufactorName%)")
    Page<Product> findProductRequestScanList(@Param("productName") String productName,
                                             @Param("manufactorName") String manufactorName,
                                             @Param("productId") int productId,
                                             Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p " +
            "JOIN p.imageProducts ip " +
            "WHERE ip.type = 1 ")
    int countImageType1ByProductId(int productId);

    @Query("SELECT new fpt.CapstoneSU24.dto.ProductResponseCustomDTO(p.productId, p.productName, c.name, u.profileImage, p.manufacturer.org_name,p.manufacturer.userId) " +
            "FROM Product p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.manufacturer u " +
            "WHERE p.productId = :id")
    ProductResponseCustomDTO findDetailProductAndUser(@Param("id") Integer id);
}

