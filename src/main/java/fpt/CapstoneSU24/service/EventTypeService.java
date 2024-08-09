package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.EventType;
import fpt.CapstoneSU24.repository.EventTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeService {
    private final EventTypeRepository eventTypeRepository;
    public EventTypeService(EventTypeRepository eventTypeRepository){
        this.eventTypeRepository = eventTypeRepository;
    }

    public ResponseEntity<?> getListEventType() {
        List<EventType> eventTypeList = eventTypeRepository.getAllEventTypeIgnoreEdit();
        return ResponseEntity.ok(eventTypeList);
    }
}
