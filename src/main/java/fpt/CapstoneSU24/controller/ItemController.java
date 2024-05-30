package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ItemLogDTO;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.LocationRepository;
import fpt.CapstoneSU24.repository.OriginRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/item")
@RestController
public class ItemController {
    public LocationRepository locationRepository;
    public OriginRepository originRepository;
   // public ItemR
    @GetMapping("/addItem")
    public ResponseEntity addItem(@RequestBody ItemLogDTO itemLogDTO) {
        locationRepository.save(itemLogDTO.getLocation());
        originRepository.save(itemLogDTO.getOrigin());

        return ResponseEntity.ok("ok");
    }
}
