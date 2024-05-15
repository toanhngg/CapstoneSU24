package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    List<Category> findAll();
    Category findOneByidCategory(int id);
}
