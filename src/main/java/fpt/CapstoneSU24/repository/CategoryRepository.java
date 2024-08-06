package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.dto.CategoryForManagerDTO;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    List<Category> findAll();
    Category findOneByCategoryId(int id);
    Category findOneByName(String name);
    void deleteOneByCategoryId(int categoryId);
    Page<Category> findAllByNameContaining(String currentOwner, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Product o JOIN Category c ON o.category.categoryId = c.categoryId WHERE o.manufacturer.userId = :id")
    List<Category> findCategoryByManufacturer(@Param("id")  int id);

    @Query("SELECT new fpt.CapstoneSU24.dto.CategoryForManagerDTO(c.categoryId, c.name, " +
            "CASE WHEN p IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM Category c LEFT JOIN Product p ON c.categoryId = p.category.categoryId " +
            "GROUP BY c.categoryId, c.name")

    List<CategoryForManagerDTO> findCategoryForManagerDTOs();

    @Query("SELECT c FROM Category c LEFT JOIN c.products p WHERE p IS NULL")
    List<Category> findCategoriesWithoutProducts();

    @Query("SELECT MAX(c.id) FROM Category c")
    Long findMaxId();
}
