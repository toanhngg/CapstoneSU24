package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.EventType;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    public EventType findOneByEventId(int id);

    @Query("SELECT ev FROM EventType ev  WHERE ev.eventId <> 6  ")
    List<EventType> getAllEventTypeIgnoreEdit();

}
