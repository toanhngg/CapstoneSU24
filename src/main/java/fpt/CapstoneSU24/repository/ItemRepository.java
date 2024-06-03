package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Override
    Page<Item> findAll(Pageable pageable);
    Page<Item> findAllByCurrentOwnerContaining(String currentOwner, Pageable pageable);
    Page<Item> findByCreatedAtBetween(Long startDate, Long endDate, Pageable pageable);

}