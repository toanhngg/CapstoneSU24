package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.payload.IdRequest;
import fpt.CapstoneSU24.service.EventTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/eventtype")
@RestController
public class EventTypeController {
    private final EventTypeService eventTypeService;
    @Autowired
    public  EventTypeController(EventTypeService eventTypeService){
        this.eventTypeService = eventTypeService;
    }
    @GetMapping("/getListEventType")
    public ResponseEntity<?> getListEventType()
    {
        return eventTypeService.getListEventType();
    }

}
