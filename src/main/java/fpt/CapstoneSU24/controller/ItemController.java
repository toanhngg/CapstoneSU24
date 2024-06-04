package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ItemDTO;
import fpt.CapstoneSU24.dto.ItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogResponse;
import fpt.CapstoneSU24.dto.OriginDTO;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.service.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/item")
@RestController
public class ItemController {
    @Autowired
    public LocationRepository locationRepository;
    @Autowired
    public OriginRepository originRepository;
    @Autowired
    public ItemResponsitory itemResponsitory;
    @Autowired
    public PartyRepository partyRepository;
    @Autowired
    public ItemLogRepository itemLogRepository;
    @Autowired
    public QRCodeGenerator qrCodeGenerator;
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    private ImageProductRepository imageProductRepository;

    @PostMapping("/addItem")
    public ResponseEntity addItem(@RequestBody ItemLogDTO itemLogDTO) {
        try {
            Location location = new Location();
            location.setAddress(itemLogDTO.getAddress());
            location.setCity(itemLogDTO.getCity());
            location.setCountry(itemLogDTO.getCountry());
            location.setCoordinateX(itemLogDTO.getCoordinateX());
            location.setCoordinateY(itemLogDTO.getCoordinateY());
            Location savedLocation = locationRepository.save(location);

            long scoreTime = System.currentTimeMillis();
            Origin origin = new Origin();
            origin.setCreatedAt(scoreTime);
            origin.setDescription(itemLogDTO.getDescriptionOrigin());
            origin.setEmail(itemLogDTO.getEmail());
            origin.setFullNameManufacturer(itemLogDTO.getFullName());
            origin.setOrg_name(itemLogDTO.getOrgName());
            origin.setPhone(itemLogDTO.getPhone());
            origin.setSupportingDocuments(itemLogDTO.getSupportingDocuments());
            origin.setLocation(savedLocation);
            Origin saveOrigin = originRepository.save(origin);

            Item item = new Item();
            item.setCreatedAt(scoreTime);
            item.setCurrentOwner(itemLogDTO.getEmail());
            //item.setProductRecognition(qrCodeGenerator.generateProductCode(itemLogDTO.getProductId()));
            item.setStatus(-1);
            item.setOrigin(saveOrigin);
            item.setProduct(productRepository.findOneByProductId(itemLogDTO.getProductId()));
            Item saveItem = itemResponsitory.save(item);

            Party party = new Party();
            party.setDescription(itemLogDTO.getDescriptionParty());
            party.setEmail(itemLogDTO.getEmail());
            party.setPartyFullName(itemLogDTO.getFullName());
            party.setPhoneNumber(itemLogDTO.getPhone());
            party.setSignature(itemLogDTO.getSignature());
            Party saveParty = partyRepository.save(party);
//[item_id],[location_id],[party_id])
            ItemLog itemLog = new ItemLog();
            itemLog.setAddress(itemLogDTO.getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
            itemLog.setEventType(itemLogDTO.getEventType());
            itemLog.setStatus(itemLogDTO.getStatusItemLog());
            itemLog.setTimeStamp(scoreTime);
            itemLog.setItem(saveItem);
            itemLog.setLocation(savedLocation);
            itemLog.setParty(saveParty);
            itemLogRepository.save(itemLog);
            return ResponseEntity.ok("ok");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @PostMapping("/addItemByQuantity")
    public ResponseEntity addItemByQuantity(@RequestBody ItemLogDTO itemLogDTO, @RequestParam int quantity) {
        try {
            Location location = new Location();
            location.setAddress(itemLogDTO.getAddress());
            location.setCity(itemLogDTO.getCity());
            location.setCountry(itemLogDTO.getCountry());
            location.setCoordinateX(itemLogDTO.getCoordinateX());
            location.setCoordinateY(itemLogDTO.getCoordinateY());
            Location savedLocation = locationRepository.save(location);

            long scoreTime = System.currentTimeMillis();
            Origin origin = new Origin();
            origin.setCreatedAt(scoreTime);
            origin.setDescription(itemLogDTO.getDescriptionOrigin());
            origin.setEmail(itemLogDTO.getEmail()); // mai lay tu User
            origin.setFullNameManufacturer(itemLogDTO.getFullName()); // mai lay tu User
            origin.setOrg_name(itemLogDTO.getOrgName());  // mai lay tu User
            origin.setPhone(itemLogDTO.getPhone()); // mai lay tu User
            origin.setSupportingDocuments(itemLogDTO.getSupportingDocuments()); // mai lay tu User
            origin.setLocation(savedLocation);
            Origin saveOrigin = originRepository.save(origin);
            for (int i = 0; i < quantity; i++) {
                Item item = new Item();
                item.setCreatedAt(scoreTime);
                item.setCurrentOwner(itemLogDTO.getEmail());
                String productRe = qrCodeGenerator.generateProductCode(itemLogDTO.getProductId(),quantity);
                item.setProductRecognition(productRe);
                item.setStatus(-1);
                item.setOrigin(saveOrigin);
                item.setProduct(productRepository.findOneByProductId(itemLogDTO.getProductId()));
                Item saveItem = itemResponsitory.save(item);

                Party party = new Party();
                party.setDescription(itemLogDTO.getDescriptionParty());
                party.setEmail(itemLogDTO.getEmail()); // mai lay tu User
                party.setPartyFullName(itemLogDTO.getFullName()); // mai lay tu User
                party.setPhoneNumber(itemLogDTO.getPhone()); // mai lay tu User
                party.setSignature(itemLogDTO.getSignature()); // mai lay tu User
                Party saveParty = partyRepository.save(party);

                ItemLog itemLog = new ItemLog();
                itemLog.setAddress(itemLogDTO.getAddress());
                itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
                itemLog.setEventType(itemLogDTO.getEventType());
                itemLog.setStatus(itemLogDTO.getStatusItemLog());
                itemLog.setTimeStamp(scoreTime);
                itemLog.setItem(saveItem);
                itemLog.setLocation(savedLocation);
                itemLog.setParty(saveParty);
                itemLogRepository.save(itemLog);
            }
            return ResponseEntity.ok("ok");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @GetMapping("/findAllItemByProductId")
    public ResponseEntity findAllItemByProductId(@RequestParam int ProductId) {
        List<Item> itemList = itemResponsitory.findByProductId(ProductId);
        if (itemList == null) {
            return ResponseEntity.badRequest().body("Lỗi: Yêu cầu không hợp lệ");
        } else {
            return ResponseEntity.ok(itemList);
        }
    }


    //list all item_log by product_recogine
    @GetMapping("/viewLineItem")
    public ResponseEntity viewLineItem(@RequestParam String productRecognition) {
        try {
            Item item = itemResponsitory.findByProductRecognition(productRecognition);
            System.out.println(item.getItemId());
            List<ItemLog> itemList = itemLogRepository.getItemLogsByItemId(item.getItemId());

            if (itemList.isEmpty()) {
                // Trả về chỉ thông tin của Item nếu không có ItemLog nào
              //  ItemDTO itemDTO = convertToItemDTO(item);
                return ResponseEntity.ok(new ItemLogResponse(/*itemDTO, */Collections.emptyList()));
            } else {
              //  ItemDTO itemDTO = convertToItemDTO(item);
                List<ItemLogDTO> itemLogDTOs = itemList.stream()
                        .map(this::convertToItemLogDTO)
                        .collect(Collectors.toList());
                // Tạo đối tượng ItemLogResponse và trả về
                ItemLogResponse itemLogResponse = new ItemLogResponse(/*itemDTO,*/ itemLogDTOs);
                return ResponseEntity.ok(itemLogResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private ItemLogDTO convertToItemLogDTO(ItemLog itemLog) {
        ItemLogDTO dto = new ItemLogDTO();
        dto.setItemLogId(itemLog.getItemLogId());
        dto.setAddress(itemLog.getAddress());
        dto.setDescriptionItemLog(itemLog.getDescription());
        return dto;
    }

    private ItemDTO convertToItemDTO(Item item) {
    ItemDTO dto = new ItemDTO();
    dto.setProductRecognition(item.getProductRecognition());
    return dto;
}

@GetMapping("/viewOrigin")
public ResponseEntity getOrigin(@RequestParam int itemLogId ) {

        ItemLog itemLog = itemLogRepository.getItemLogs(itemLogId);

    OriginDTO originDTO = new OriginDTO();
    originDTO.setCreateAt(itemLog.getItem().getCreatedAt());
    originDTO.setProductName(itemLog.getItem().getProduct().getProductName());
    originDTO.setProductRecognition(itemLog.getItem().getProductRecognition());
    originDTO.setOrgName(itemLog.getItem().getOrigin().getOrg_name());
    originDTO.setPhone(itemLog.getItem().getOrigin().getPhone());
    originDTO.setFullName(itemLog.getItem().getOrigin().getFullNameManufacturer());
    originDTO.setDescriptionProduct(itemLog.getItem().getProduct().getDescription());
    originDTO.setDescriptionOrigin(itemLog.getItem().getOrigin().getDescription());
    originDTO.setWarranty(itemLog.getItem().getProduct().getWarranty());
    Integer productId = itemLog.getItem().getProduct().getProductId();
    if(productId == null){
        originDTO.setImage(null);
    }else {
        ImageProduct imageProduct = imageProductRepository.findByCurrentOwner(itemLog.getItem().getProduct().getProductId());
        if(imageProduct == null){
            originDTO.setImage(null);

        }else{
            byte[] img = imageProduct.getImage();
            originDTO.setImage(img);

        }

    }
    if (originDTO != null) {
        return new ResponseEntity<>(originDTO, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

    @PostMapping(value = "searchByCurrentOwner")
    public ResponseEntity searchByCurrentOwner(@RequestParam String currentOwner) {
    List<Item> itemList = itemResponsitory.findByCurrentOwner(currentOwner);
        return ResponseEntity.status(200).body(itemList);
    }

}
