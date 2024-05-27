package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Actor;
import fpt.CapstoneSU24.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
        }
