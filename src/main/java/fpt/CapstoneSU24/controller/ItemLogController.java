package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.EventItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.dto.Point;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.service.ClientService;
import fpt.CapstoneSU24.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    public TransportRepository transportRepository;
    @Autowired
    public AuthorizedRepository authorizedRepository;
    public PointService pointService;

    @PostMapping(value = "/additemlogTransport")
    public ResponseEntity<String> addItemLog(@RequestBody EventItemLogDTO itemLogDTO) {
        try {
            // Retrieve item by product recognition
            Item item = itemResponsitory.findByProductRecognition(itemLogDTO.getProductRecognition());
            if (item == null) {
                return new ResponseEntity<>("Item not found.", HttpStatus.NOT_FOUND);
            }

            // Get item logs by item ID
            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return new ResponseEntity<>("Item log list does not have enough elements.", HttpStatus.BAD_REQUEST);
            }

            ItemLog itemLogToCheck = list.get(0);
            if (itemLogToCheck.getStatus() != 0) {
                return new ResponseEntity<>("Item log status is not valid.", HttpStatus.BAD_REQUEST);
            }

            // Create and save Location
            Location location = new Location();
            location.setAddress(itemLogDTO.getAddress());
            location.setCity(itemLogDTO.getCity());
            location.setCountry(itemLogDTO.getCountry());
            location.setCoordinateX(itemLogDTO.getCoordinateX());
            location.setCoordinateY(itemLogDTO.getCoordinateY());
            Location savedLocation = locationRepository.save(location);

            // Retrieve authorized entity
            Authorized authorized = authorizedRepository.getReferenceById(itemLogToCheck.getAuthorized().getAuthorized_id());
            if (authorized == null) {
                return new ResponseEntity<>("Authorized entity not found.", HttpStatus.NOT_FOUND);
            }

            // Create Party if eventId is 2
            Party party = new Party();
            if (itemLogDTO.getEventId() == 2) {
                Transport transport = transportRepository.getReferenceById(itemLogDTO.getTransportId());
                if (transport == null) {
                    return new ResponseEntity<>("Transport entity not found.", HttpStatus.NOT_FOUND);
                }
                party.setEmail(transport.getTransportEmail());
                party.setPartyFullName(transport.getTransportName());
                party.setPhoneNumber(transport.getTransportContact());
                party.setDescription(itemLogDTO.getDescriptionItemLog());
            } else {
                party.setEmail("");
                party.setPartyFullName("");
                party.setPhoneNumber("");
                party.setDescription("");
            }
            Party savedParty = partyRepository.save(party);

            // Create and save ItemLog
            ItemLog itemLog = new ItemLog();
            itemLog.setAddress(itemLogDTO.getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
            itemLog.setAuthorized(authorized);
            itemLog.setStatus(0);
            itemLog.setTimeStamp(System.currentTimeMillis());
            itemLog.setItem(itemLogToCheck.getItem());
            itemLog.setLocation(savedLocation);
            itemLog.setParty(savedParty);
            itemLog.setEvent_id(eventTypeRepository.findOneByEventId(itemLogDTO.getEventId()));
            Point point = pointService.randomPoint();
            itemLog.setPoint(point.toString());
            itemLogRepository.save(itemLog);
            return new ResponseEntity<>("Add successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/getItemLogDetail")
    public ResponseEntity getItemLogDetail(@RequestParam int itemLogId) {
        try {
            ItemLog itemlogDetail = itemLogRepository.getItemLogsById(itemLogId);
            if(itemlogDetail == null)
                return new ResponseEntity<>("ItemLog not found.", HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(detailResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }
}
