package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ItemLogDTO;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.service.QRCodeGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/item")
@RestController
public class ItemController {
    public LocationRepository locationRepository;
    public OriginRepository originRepository;
   public ItemResponsitory itemResponsitory;
   public PartyRepository partyRepository;
   public ItemLogRepository itemLogRepository;
   public QRCodeGenerator qrCodeGenerator;
    @PostMapping("/addItem")
    public ResponseEntity addItem(@RequestBody ItemLogDTO itemLogDTO) {
        Location savedLocation = locationRepository.save(itemLogDTO.getLocation());

        Origin origin = new Origin();
        origin.setCreatedAt(11111111);
        origin.setDescription(itemLogDTO.getOrigin().getDescription());
        origin.setEmail(itemLogDTO.getOrigin().getEmail());
        origin.setFullNameManufacturer(itemLogDTO.getOrigin().getFullNameManufacturer());
        origin.setOrg_name(itemLogDTO.getOrigin().getOrg_name());
        origin.setPhone(itemLogDTO.getOrigin().getPhone());
        origin.setSupportingDocuments(itemLogDTO.getOrigin().getSupportingDocuments());
        origin.setLocation(savedLocation);
        Origin saveOrigin = originRepository.save(origin);

        Item item = new Item();
        item.setCreatedAt(111111111);
        item.setCurrentOwner(itemLogDTO.getItem().getCurrentOwner());
        item.setProductRecognition(qrCodeGenerator.generateProductCode(itemLogDTO.getItem().getProduct().getProductName(), itemLogDTO.getItem().getProduct().getCategory().getName()));
        item.setStatus(itemLogDTO.getItem().getStatus());
        item.setOrigin(saveOrigin);
        item.setProduct(itemLogDTO.getItem().getProduct());
        Item saveItem = itemResponsitory.save(item);

        Party party = new Party();
        party.setDescription(itemLogDTO.getParty().getDescription());
        party.setEmail(itemLogDTO.getParty().getEmail());
        party.setPartyFullName(itemLogDTO.getParty().getPartyFullName());
        party.setPhoneNumber(itemLogDTO.getParty().getPhoneNumber());
        party.setSignature(itemLogDTO.getParty().getSignature());
        Party saveParty = partyRepository.save(party);
//[item_id],[location_id],[party_id])
        ItemLog itemLog = new ItemLog();
        itemLog.setAddress(itemLogDTO.getItemLog().getAddress());
        itemLog.setDescription(itemLogDTO.getItemLog().getDescription());
        itemLog.setEventType(itemLogDTO.getItemLog().getEventType());
        itemLog.setStatus(itemLogDTO.getItemLog().getStatus());
        itemLog.setTimeStamp(itemLogDTO.getItemLog().getTimeStamp());
        itemLog.setItem(saveItem);
        itemLog.setLocation(savedLocation);
        itemLog.setParty(saveParty);
        itemLogRepository.save(itemLogDTO.getItemLog());
        return ResponseEntity.ok("ok");
    }

    //@PostMapping(/)
}
