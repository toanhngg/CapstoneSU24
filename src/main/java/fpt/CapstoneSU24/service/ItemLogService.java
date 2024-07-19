package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.EditItemLogDTO;
import fpt.CapstoneSU24.dto.EventItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.dto.Point;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.LocationMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class ItemLogService {
    private final LocationRepository locationRepository;
    private final ItemRepository itemRepository;
    private final PartyRepository partyRepository;
    private final ItemLogRepository itemLogRepository;
    private final EventTypeRepository eventTypeRepository;
    private final TransportRepository transportRepository;
    private final AuthorizedRepository authorizedRepository;
    private final PointService pointService;
    private final ItemService itemService;
    private final LogService logService;
    private final LocationMapper locationMapper;

    @Autowired
    public ItemLogService(LocationRepository locationRepository,
                             ItemRepository itemRepository, PartyRepository partyRepository,
                             ItemLogRepository itemLogRepository,
                             EventTypeRepository eventTypeRepository, TransportRepository transportRepository,
                             AuthorizedRepository authorizedRepository, PointService pointService,
                             ItemService itemService,LogService logService,LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.itemRepository = itemRepository;
        this.partyRepository = partyRepository;
        this.itemLogRepository = itemLogRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.transportRepository = transportRepository;
        this.authorizedRepository = authorizedRepository;
        this.pointService = pointService;
        this.itemService = itemService;
        this.logService = logService;
        this.locationMapper = locationMapper;
    }
    private static final Logger log = LoggerFactory.getLogger(ItemLogService.class);

    public ResponseEntity<?> addItemLog(EventItemLogDTO itemLogDTO) {
        try {
            log.info("itemlog-addItemLog");
            // Retrieve item by product recognition
            Item item = itemRepository.findByProductRecognition(itemLogDTO.getProductRecognition());
            if (item == null) {
                return new ResponseEntity<>("Item not found.", HttpStatus.NOT_FOUND);
            }

            if (!itemService.checkOwner(itemLogDTO.getEmailParty(), item.getCurrentOwner())) {
                return new ResponseEntity<>("Unauthorized action.", HttpStatus.UNAUTHORIZED);
            }

            // Get item logs by item ID
            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return new ResponseEntity<>("Item log list does not have enough elements.", HttpStatus.BAD_REQUEST);
            }

            ItemLog itemLogToCheck = list.get(0);

            // Create and save Location
//            Location location = new Location();
//            location.setAddress(itemLogDTO.getAddress());
//            location.setCity(itemLogDTO.getCity());
//            location.setCountry(itemLogDTO.getCountry());
//            location.setCoordinateX(itemLogDTO.getCoordinateX());
//            location.setCoordinateY(itemLogDTO.getCoordinateY());
//            Location savedLocation = locationRepository.save(location);
            Location savedLocation = locationRepository.save(locationMapper.locationDtoToLocation(itemLogDTO.getLocation()));

            // Retrieve authorized entity
            Authorized authorized = authorizedRepository.getReferenceById(itemLogToCheck.getAuthorized().getAuthorizedId());

            // Create and save Party
            Party party = new Party();
            if (itemLogDTO.getEventId() == 2) {
                if (itemLogDTO.getTransportId() == 0) {
                    return new ResponseEntity<>("Please choose a carrier.", HttpStatus.BAD_REQUEST);
                }
                Transport transport = transportRepository.getReferenceById(itemLogDTO.getTransportId());
                party.setEmail(transport.getTransportEmail());
                party.setPartyFullName(transport.getTransportName());
                party.setPhoneNumber(transport.getTransportContact());
                party.setDescription(itemLogDTO.getDescriptionItemLog());
            } else {
                party.setEmail("");
                party.setPartyFullName("");
                party.setPhoneNumber("");
                party.setDescription("Khác");
            }
            Party savedParty = partyRepository.save(party);

            // Create and save ItemLog
            ItemLog itemLog = new ItemLog();
            itemLog.setAddress(itemLogDTO.getLocation().getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
            itemLog.setAuthorized(authorized);
            itemLog.setStatus(0);
            itemLog.setTimeStamp(System.currentTimeMillis());
            itemLog.setItem(item);
            itemLog.setLocation(savedLocation);
            itemLog.setParty(savedParty);
            itemLog.setEvent_id(eventTypeRepository.findOneByEventId(itemLogDTO.getEventId()));

            // Additional validation
            if (itemLogDTO.getLocation().getAddress() != null &&
                    itemLogDTO.getLocation().getCity() != null  &&
                    itemLogDTO.getLocation().getCountry() != null  &&
                    itemLogToCheck.getItem() != null) {

                double pointX = pointService.generateX();
                List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                List<Point> pointList = pointService.getPointList(pointLogs);
                double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                Point point = new Point(pointX, pointY);
                itemLog.setPoint(point.toString());
            }

            itemLogRepository.save(itemLog);
            return new ResponseEntity<>("Add successfully.", HttpStatus.OK);

        } catch (Exception ex) {
            logService.logError(ex);
            return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getItemLogDetail(int itemLogId) {
        try {
            log.info("itemlog-getItemLogDetail");

            ItemLog itemlogDetail = itemLogRepository.getItemLogsById(itemLogId);
            if(itemlogDetail == null)
                return new ResponseEntity<>("ItemLog not found.", HttpStatus.NOT_FOUND);
            ItemLogDetailResponse detailResponse = new ItemLogDetailResponse();
            detailResponse.setItemLogId(itemlogDetail.getItemLogId());
            detailResponse.setEventType(itemlogDetail.getEvent_id().getEvent_type());
            detailResponse.setSender(itemlogDetail.getAuthorized().getAssignPersonMail());
            detailResponse.setReceiver(itemlogDetail.getAuthorized().getAuthorizedEmail());
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

    public ResponseEntity<?> editItemLog( EditItemLogDTO dataEditDTO) {
        log.info("itemlog-editItemLog");

        ItemLog itemlogDetail = itemLogRepository.findById((Integer)dataEditDTO.getItemLogId())
                .orElseThrow(() -> new RuntimeException("ItemLog not found"));
        Item item = itemRepository.getReferenceById(itemlogDetail.getItem().getItemId());

        if(itemService.checkOwner(dataEditDTO.getEmail(),item.getCurrentOwner())) {
            Location location = locationRepository.findOneByItemLogId(dataEditDTO.getItemLogId());
            locationRepository.updateLocation(location.getLocationId(),dataEditDTO.getLocation().getAddress(),dataEditDTO.getLocation().getCity(),
                    dataEditDTO.getLocation().getCountry(),dataEditDTO.getLocation().getCoordinateX(),dataEditDTO.getLocation().getCoordinateY(),
                    dataEditDTO.getLocation().getDistrict(),dataEditDTO.getLocation().getWard());

            if (hasNullFields(itemlogDetail)) {
                double pointX = pointService.generateX();
                List<ItemLog> pointLogs = itemLogRepository.getPointItemId(itemlogDetail.getItem().getItemId());
                List<Point> pointList = pointService.getPointList(pointLogs);
                double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                Point point = new Point(pointX, pointY);
                itemlogDetail.setPoint(point.toString());
            }
            // Save the updated ItemLog
            itemLogRepository.save(itemlogDetail);


            return ResponseEntity.status(HttpStatus.OK).body("Edit successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail!");
    }

    public boolean hasNullFields(ItemLog itemLog) {
        for (Field field : itemLog.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            try {
                if (field.get(itemLog) == null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        return false;
    }

//    public ResponseEntity<?> getItemLogsByItemId(int itemId) {
//        List<ItemLog> itemLogs = itemLogRepository.getItemLogsByItemId(itemId);
//        if (itemLogs.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ItemLogs found for itemId: " + itemId);
//        } else {
//            return ResponseEntity.ok(itemLogs.get(0).getEvent_id().getEvent_type());
//        }
//    }
//    public ResponseEntity<?> getItemLogsLocationByItemId(int itemId) {
//        List<ItemLog> itemLogs = itemLogRepository.getItemLogsByItemId(itemId);
//        if (itemLogs.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ItemLogs found for itemId: " + itemId);
//        } else {
//            return ResponseEntity.ok(itemLogs.get(0).getLocation().getAddress());
//        }
//    }

    public ResponseEntity<?> getEventByItemId(int itemId) {
        log.info("itemlog-getEventByItemId");
        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(itemId);
        return itemLogOptional.<ResponseEntity<?>>map(itemLog -> ResponseEntity.ok(itemLog.getEvent_id().getEventId())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ItemLogs found for itemId: " + itemId));
    }
    public ResponseEntity<?> getLocationItemId(int itemId) {
        log.info("itemlog-getLocationItemId");

        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(itemId);
        return itemLogOptional.<ResponseEntity<?>>map(itemLog -> ResponseEntity.ok(itemLog.getLocation())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Location found for itemId: " + itemId));
    }

}
