package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.model.EventType;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.model.Party;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/itemlog")
@RestController
public class ItemLogController {
    //select * from item i left join item_log il on i.item_id = il.item_id
    @Autowired
    public LocationRepository locationRepository;
    @Autowired
    public OriginRepository originRepository;
    @Autowired
    public ItemRepository itemResponsitory;
    @Autowired
    public PartyRepository partyRepository;
    @Autowired
    public ItemLogRepository itemLogRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    public EventTypeRepository eventTypeRepository;


//    @PostMapping(value = "/additemlog") // SAU ADD SHIPPER
//    public ResponseEntity addItemLog(@RequestBody ItemLogDTO itemLogDTO) {
//        try {
//
////            Location location = new Location();
////            location.setAddress(itemLogDTO.getAddress());
////            location.setCity(itemLogDTO.getCity());
////            location.setCountry(itemLogDTO.getCountry());
////            location.setCoordinateX(itemLogDTO.getCoordinateX());
////            location.setCoordinateY(itemLogDTO.getCoordinateY());
////
////            Location locationId = locationRepository.save(location);
//
//            Party party = new Party();
//            party.setDescription(itemLogDTO.getDescriptionParty());
//            party.setEmail(itemLogDTO.getEmail());
//            party.setPartyFullName(itemLogDTO.getFullName());
//            party.setSignature(itemLogDTO.getSignature());
//
//            Party partyId = partyRepository.save(party);
//
//            ItemLog itemLog = new ItemLog();
//            itemLog.setAddress(itemLogDTO.getAddress());
//            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
//            itemLog.setEventType(itemLogDTO.getEventType());
//            itemLog.setStatus(itemLogDTO.getStatusItemLog());
//            itemLog.setTimeStamp(System.currentTimeMillis());
//            //itemLog.setLocation(locationId);
//            itemLog.setParty(partyId);
//
//            itemLogRepository.save(itemLog);
//            ClientSdi sdi = new ClientSdi();
//            sdi.setName(itemLogDTO.getFullName());
//            sdi.setEmail(itemLogDTO.getEmail());
//            clientService.notification(sdi);
//            // add bảng location, item_log and party
//            return ResponseEntity.status(200).body("Add successfully");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

//    @PostMapping(value = "/additemlogAuthor")
//    public ResponseEntity additemlogAuthor(@RequestBody ItemLogDTO itemLogDTO) {
//        try {
//            Location location = new Location();
//            location.setAddress(itemLogDTO.getAddress());
//            location.setCity(itemLogDTO.getCity());
//            location.setCountry(itemLogDTO.getCountry());
//            location.setCoordinateX(itemLogDTO.getCoordinateX());
//            location.setCoordinateY(itemLogDTO.getCoordinateY());
//
//            Location locationId = locationRepository.save(location);
//
//            Party party = new Party();
//            party.setDescription(itemLogDTO.getDescriptionParty());
//            party.setEmail(itemLogDTO.getEmail());
//            party.setPartyFullName(itemLogDTO.getFullName());
//            party.setSignature(itemLogDTO.getSignature());
//
//            Party partyId = partyRepository.save(party);
//
//            ItemLog itemLog = new ItemLog();
//            itemLog.setAddress(itemLogDTO.getAddress());
//            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
//            itemLog.setEventType(itemLogDTO.getEventType());
//            itemLog.setStatus(itemLogDTO.getStatusItemLog());
//            itemLog.setTimeStamp(System.currentTimeMillis());
//            itemLog.setLocation(locationId);
//            itemLog.setParty(partyId);
//
//            itemLogRepository.save(itemLog);
//            ClientSdi sdi = new ClientSdi();
//            sdi.setName(itemLogDTO.getFullName());
//            sdi.setEmail(itemLogDTO.getEmail());
//
//            //send mail
//            clientService.notification(sdi);
//            // add bảng location, item_log and party
//            return ResponseEntity.status(200).body("Uy quyen thanh cong!");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

    @GetMapping(value = "/getItemLogDetail")
    public ResponseEntity getItemLogDetail(@RequestParam int itemLogId) {
        try {
          ItemLog itemlogDetail =  itemLogRepository.getItemLogsById(itemLogId);
            ItemLogDetailResponse detailResponse = new ItemLogDetailResponse();
            detailResponse.setItemLogId(itemlogDetail.getItemLogId());
            detailResponse.setEventType(itemlogDetail.getEvent_id().getEvent_type());
            detailResponse.setSender(itemlogDetail.getAuthorized().getAssign_person_mail());
            detailResponse.setReceiver(itemlogDetail.getAuthorized().getAuthorized_email());
            detailResponse.setPartyFullname(itemlogDetail.getParty().getPartyFullName());
            detailResponse.setPartyPhoneNumber(itemlogDetail.getParty().getPhoneNumber());
            detailResponse.setAddressInParty(itemlogDetail.getAddress());
            detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
            detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
            detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
            detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());

            return ResponseEntity.ok().body(detailResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



}
