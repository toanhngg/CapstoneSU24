package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.EventType;
import fpt.CapstoneSU24.model.ItemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  EventTypeRepository extends JpaRepository<EventType, Integer> {
}
