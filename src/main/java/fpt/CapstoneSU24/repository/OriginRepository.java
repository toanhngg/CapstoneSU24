package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OriginRepository extends JpaRepository<Origin, Integer> {
    @Override
    List<Origin> findAll();
}
