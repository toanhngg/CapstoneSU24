package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Override
    List<Item> findAll();
    public Item findOneById(int id);
}
