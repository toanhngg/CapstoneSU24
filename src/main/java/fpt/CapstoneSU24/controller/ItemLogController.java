package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/itemlog")
@RestController
public class ItemLogController {
    //select * from item i left join item_log il on i.item_id = il.item_id
    public LocationRepository locationRepository;
    public OriginRepository originRepository;
    public ItemResponsitory itemResponsitory;
    public PartyRepository partyRepository;
    public ItemLogRepository itemLogRepository;
    @PostMapping(value = "/additemlog")
    public ResponseEntity addItemLog(@RequestBody String req) {

        // add bảng location, item_log and party
return ResponseEntity.status(200).body("add sucessfully");
    }
}
