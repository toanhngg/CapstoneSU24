package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    List<Category> findAll();
}
