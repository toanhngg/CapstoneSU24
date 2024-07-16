package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.*;
import fpt.CapstoneSU24.dto.payload.FilterSearchItemRequest;
import fpt.CapstoneSU24.dto.payload.FilterByTimeStampRequest;
import fpt.CapstoneSU24.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RequestMapping("/api/item")
@RestController
public class ItemController {

    //private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /*
     * type is sort type: "desc" or "asc"
     * default data startDate and endDate equal 0 (need insert 2 data)
     * */
    @PostMapping("/search")
    public ResponseEntity<?> searchItem(@Valid @RequestBody FilterSearchItemRequest req) {
        return itemService.searchItem(req);
    }

    @PostMapping("/exportListItem")
    public ResponseEntity<?> exportListItem(@Valid @RequestBody FilterByTimeStampRequest req) throws IOException {
        return itemService.exportListItem(req);
    }

    @PostMapping("/addItem")
    public ResponseEntity<?> addItem(@Valid @RequestBody ItemLogDTO itemLogDTO) {
        return itemService.addItem(itemLogDTO);
    }

//    private double parseCoordinate(String coordinate) throws InvalidCoordinateException {
//        try {
//            return Double.parseDouble(coordinate);
//        } catch (NumberFormatException ex) {
//            throw new InvalidCoordinateException("Invalid coordinate: " + coordinate);
//        }
//    }

//    @GetMapping("/findAllItemByProductId")
//    public ResponseEntity<?> findAllItemByProductId(@RequestParam int ProductId) {
//        return itemService.findByProductId(ProductId);
//    }


    //list all item_log by product_recogine
    @GetMapping("/viewLineItem")
    public ResponseEntity<?> viewLineItem(@RequestParam String productRecognition) {
        return itemService.viewLineItem(productRecognition);

    }

    @GetMapping("/viewOrigin")
    public ResponseEntity<?> viewOrigin(@RequestParam int itemLogId) {
        return itemService.viewOrigin(itemLogId);
    }

    @GetMapping("/getCertificate")
    public ResponseEntity<?> getCertificate( @Valid @RequestBody CurrentOwnerCheckDTO req) {
         return itemService.getCertificate(req);
    }

    @PostMapping(value = "/confirmCurrentOwner")
    public ResponseEntity<Boolean> confirmCurrentOwner(@RequestBody SendOTP otp, @RequestParam String productRecognition) {
        // B1: Người dùng nhập OTP confirm chính xác bằng cách check OTP trong DB và người dùng nhập
        // - Chính xác => Buoc tiep theo
        return itemService.confirmCurrentOwner(otp,productRecognition);
    }

    /**
     * API uy quyen checkCurrentOwner => authorized
     * Cai nay la uy quyen nguoi nhan
     * B1. Kiem tra xem san pham nay da duoc uy quyen chua bang cach check currentOwner voi status la 0
     * - Neu ma co currentOwner roi va co status la 1 thi co nghia la chua uy quyen
     * - Neu ma co currentOwner roi va co status la 0 thi co nghia la da uy quyen roi => khong cho uy quyen nua
     * B2. Neu chua uy quyen thi
     * - Update currentOwner voi email cua nguoi duoc uy quyen va status la 0
     * - Update bang itemLog voi id dai nhat voi authorized_id va bang authorized nhung thong tin cua nguoi duoc uy quyen
     * - Gui mail thong bao cho nguoi dung la bạn da duoc uy quyen
     */
    @PostMapping(value = "/authorized")
    public ResponseEntity<?> authorize(@Valid @RequestBody AuthorizedDTO authorized) {
        return itemService.authorize(authorized);
    }

    @PostMapping(value = "/checkEventAuthorized")
    public ResponseEntity<Boolean> checkEventAuthorized(@RequestParam String productRecognition)  {
        if(productRecognition.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        return itemService.checkEventAuthorized(productRecognition);
        // B1. Kiểm tra xem email này có phải currentOwner với status là 1 không
        // - Nếu mà không phải currentOwner => không cho ủy quyền người tiếp theo
    }
//    @PostMapping(value = "/checkAuthorized")
//    public ResponseEntity<Boolean> checkAuthorized(@Valid @RequestBody CurrentOwnerCheckDTO req)  {
//        return itemService.checkAuthorized(req);
//    }
    @PostMapping(value = "/check")
    public ResponseEntity<Integer> check(@Valid @RequestBody CurrentOwnerCheckDTO req)  {
        return itemService.check(req);
    }
    //API check CurrentOwner
//    @PostMapping(value = "/checkCurrentOwner")
//    public ResponseEntity<Boolean> checkCurrentOwner(@Valid @RequestBody CurrentOwnerCheckDTO req) {
//        // B1. Kiểm tra xem email này có phải currentOwner với status là 1 không
//        // - Nếu mà không phải currentOwner => không cho ủy quyền người tiếp theo
//        return itemService.checkCurrentOwner(req);
//    }

    @PostMapping(value = "/sendCurrentOwnerOTP")
    public ResponseEntity<?> sendCurrentOwnerOTP(@Valid @RequestBody CurrentOwnerCheckDTO req) {
        return itemService.sendCurrentOwnerOTP(req);
    }

    //API send OTP  nhap mail => sendOTP chua check
    @PostMapping(value = "/sendOTP")
    public ResponseEntity<?> sendOTP(@Valid @RequestBody CurrentOwnerCheckDTO req) {
        return itemService.sendOTP(req);
    }

    // API verify OTP
    @PostMapping(value = "/confirmOTP")
    public ResponseEntity<Boolean> confirmOTP(@Valid @RequestBody SendOTP otp, @RequestParam String productRecognition) {
        if(productRecognition.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        return itemService.confirmOTP(otp,productRecognition);
    }
    @PostMapping(value = "/abortItem")
    public ResponseEntity<?> abortItem(@RequestBody AbortDTO abortDTO ){
        return itemService.abortItem(abortDTO);
    }

    @GetMapping(value = "/getItemByEventType")
    public ResponseEntity<?> getItemByEventType(int eventType){
        return itemService.getItemByEventType(eventType);
    }

}

