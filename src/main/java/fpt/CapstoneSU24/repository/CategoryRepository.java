package fpt.CapstoneSU24.repository;

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
}
