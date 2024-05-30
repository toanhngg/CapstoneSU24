package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemResponsitory extends JpaRepository<Item, Integer> {
}
