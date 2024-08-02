package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.EditItemLogDTO;
import fpt.CapstoneSU24.dto.EditItemLogPartyDTO;
import fpt.CapstoneSU24.dto.EventItemLogDTO;
import fpt.CapstoneSU24.dto.UpdateItemLogDTO;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.service.ItemLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/itemlog")
@RestController
public class ItemLogController {
    private final ItemLogService itemLogService;

    @Autowired
    public ItemLogController(ItemLogService itemLogService) {
        this.itemLogService = itemLogService;
    }

    @PostMapping(value = "/addItemLogTransport")
    public ResponseEntity<?> addItemLogTransport(@Valid @RequestBody EventItemLogDTO itemLogDTO) {
        return itemLogService.addItemLogTransport(itemLogDTO);
    }

    @GetMapping(value = "/getItemLogDetail")
    public ResponseEntity<?> getItemLogDetail(@RequestParam int itemLogId) {
        if(itemLogId < 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ItemLogId is not null");
        return itemLogService.getItemLogDetail(itemLogId);
    }
    @GetMapping(value = "/getItemLogDetailHistory")
    public ResponseEntity<?> getItemLogDetailHistory(@RequestParam int itemLogId) {
        if(itemLogId < 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ItemLogId is not null");
        return itemLogService.getItemLogDetailHistory(itemLogId);
    }
    //Dau sua chua update
    @PostMapping(value = "/editItemLog")
    public ResponseEntity<?> editItemLog(@Valid @RequestBody EditItemLogDTO dataEditDTO){
        return itemLogService.editItemLog(dataEditDTO);
    }
    @PostMapping(value = "/updateItemLog")
    public ResponseEntity<?> updateItemLog(@Valid @RequestBody UpdateItemLogDTO dataEditDTO){
        return itemLogService.updateItemLog(dataEditDTO);
    }
    @PostMapping(value = "/editItemLogByParty")
    public ResponseEntity<?> editItemLogByParty(@Valid @RequestBody EditItemLogPartyDTO dataEditDTO){
        return itemLogService.editItemLogByParty(dataEditDTO);
    }

    @PostMapping(value = "/editTransport")
    public ResponseEntity<?> editTransport(@Valid @RequestBody EventItemLogDTO dataEditDTO){
        return itemLogService.editTransport(dataEditDTO);
    }
//    @GetMapping(value = "getItemLogsByItemId")
//    public ResponseEntity<?>  getItemLogsByItemId(@RequestParam int itemId){
//        return itemLogService.getItemLogsByItemId(itemId);
//    }
    @GetMapping(value = "/getEventByItemId")
    public ResponseEntity<?>  getEventByItemId(@RequestParam int itemId){
        return itemLogService.getEventByItemId(itemId);
    }
    @GetMapping(value = "/getLocationItemId")
    public ResponseEntity<?>  getLocationItemId(@RequestParam int itemId){
        return itemLogService.getLocationItemId(itemId);
    }
}
