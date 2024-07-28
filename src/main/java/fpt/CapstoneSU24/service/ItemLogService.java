package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.EditItemLogDTO;
import fpt.CapstoneSU24.dto.EventItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.dto.Point;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.ItemMapper;
import fpt.CapstoneSU24.mapper.LocationMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ItemMapper itemMapper;

    @Autowired
    public ItemLogService(LocationRepository locationRepository,
                             ItemRepository itemRepository, PartyRepository partyRepository,
                             ItemLogRepository itemLogRepository,
                             EventTypeRepository eventTypeRepository, TransportRepository transportRepository,
                             AuthorizedRepository authorizedRepository, PointService pointService,
                          ItemService itemService, LogService logService, LocationMapper locationMapper,
                          ItemMapper itemMapper) {
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
        this.itemMapper = itemMapper;
    }
    private static final Logger log = LoggerFactory.getLogger(ItemLogService.class);

    public ResponseEntity<?> addItemLog(EventItemLogDTO itemLogDTO) {
        try {
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
            if (itemLogToCheck.getAuthorized() == null)
                return new ResponseEntity<>("The product needs to be in an authorized state to be able to add a carrier.", HttpStatus.BAD_REQUEST);
            if (itemLogToCheck.getEvent_id().getEventId() == 2)
                return new ResponseEntity<>("The product is in shipping status .", HttpStatus.BAD_REQUEST);

            Location savedLocation = locationRepository.save(itemLogToCheck.getLocation());

            System.out.println(itemLogToCheck.getAuthorized().getAuthorizedId());
            // Retrieve authorized entity
            Authorized authorized = authorizedRepository.getReferenceById(itemLogToCheck.getAuthorized().getAuthorizedId());

            // Create and save Party
            Party party = new Party();
                if (itemLogDTO.getTransportId() == 0) {
                    return new ResponseEntity<>("Please choose a carrier.", HttpStatus.BAD_REQUEST);
                }
                Transport transport = transportRepository.getReferenceById(itemLogDTO.getTransportId());
                party.setEmail(transport.getTransportEmail());
                party.setPartyFullName(transport.getTransportName());
                party.setPhoneNumber(transport.getTransportContact());
                party.setDescription(itemLogDTO.getDescriptionItemLog());
                System.out.println("transport-" + transport.getTransportEmail());
                log.info("transport" + transport.getTransportId());
            Party savedParty = partyRepository.save(party);

            // Create and save ItemLog
            ItemLog itemLog = new ItemLog();
            itemLog.setAddress(itemLogToCheck.getLocation().getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
            itemLog.setAuthorized(authorized);
            itemLog.setStatus(0);
            itemLog.setTimeStamp(System.currentTimeMillis());
            itemLog.setItem(item);
            itemLog.setLocation(savedLocation);
            itemLog.setParty(savedParty);
            itemLog.setEvent_id(eventTypeRepository.findOneByEventId(2));


                double pointX = pointService.generateX();
                List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                List<Point> pointList = pointService.getPointList(pointLogs);
                double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                Point point = new Point(pointX, pointY);
                itemLog.setPoint(point.toString());
            itemLogRepository.save(itemLog);
            return new ResponseEntity<>("Add successfully.", HttpStatus.OK);


        } catch (Exception ex) {
            logService.logError(ex);
            return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getItemLogDetail(int itemLogId) {
        try {
            ItemLog itemlogDetail = itemLogRepository.getItemLogsById(itemLogId);
            if(itemlogDetail == null)
                return new ResponseEntity<>("ItemLog not found.", HttpStatus.NOT_FOUND);
            ItemLogDetailResponse detailResponse = new ItemLogDetailResponse();
            detailResponse.setItemLogId(itemlogDetail.getItemLogId());
            detailResponse.setEventType(itemlogDetail.getEvent_id().getEvent_type());
            if (itemlogDetail.getEvent_id().getEventId() == 3) {
                if (itemlogDetail.getIdEdit() !=  null) {
                    detailResponse.setSender(itemlogDetail.getAuthorized().getAssignPersonMail());
                    detailResponse.setReceiver(itemlogDetail.getAuthorized().getAuthorizedEmail());
                    detailResponse.setPartyFullname(itemlogDetail.getParty().getEmail());
                    detailResponse.setPartyPhoneNumber(itemlogDetail.getParty().getPhoneNumber());
                    detailResponse.setAddressInParty(itemlogDetail.getAddress());
                    detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
                    detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
                    detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
                    detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());
                    ItemLog itemLog = itemLogRepository.getItemLogs(itemlogDetail.getIdEdit());

                    ItemLogDetailResponse nestedResponse = new ItemLogDetailResponse();
                    nestedResponse.setItemLogId(itemLog.getItemLogId());
                    nestedResponse.setEventType(itemLog.getEvent_id().getEvent_type());
                    nestedResponse.setPartyFullname(itemLog.getParty().getEmail());
                    nestedResponse.setSender(itemLog.getAuthorized().getAssignPersonMail());
                    nestedResponse.setReceiver(itemLog.getAuthorized().getAuthorizedEmail());
                    nestedResponse.setPartyPhoneNumber(itemLog.getParty().getPhoneNumber());
                    nestedResponse.setAddressInParty(itemLog.getLocation().getAddress());
                    nestedResponse.setCoordinateX(itemLog.getLocation().getCoordinateX());
                    nestedResponse.setCoordinateY(itemLog.getLocation().getCoordinateY());
                    nestedResponse.setTimeReceive(itemLog.getTimeStamp());
                    nestedResponse.setDescriptionItemLog(itemLog.getDescription());
                    // Gán nestedItemLog cho phản hồi chính
                    detailResponse.setItemLog(nestedResponse);
                }else{
                    detailResponse.setSender(itemlogDetail.getAuthorized().getAssignPersonMail());
                    detailResponse.setReceiver(itemlogDetail.getAuthorized().getAuthorizedEmail());
                    detailResponse.setPartyFullname(itemlogDetail.getParty().getEmail());
                    detailResponse.setPartyPhoneNumber(itemlogDetail.getParty().getPhoneNumber());
                    detailResponse.setAddressInParty(itemlogDetail.getAddress());
                    detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
                    detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
                    detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
                    detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());
                }
            } else if(itemlogDetail.getEvent_id().getEventId() == 2)  {
                detailResponse.setSender(itemlogDetail.getAuthorized().getAssignPersonMail());
                detailResponse.setReceiver(itemlogDetail.getAuthorized().getAuthorizedEmail());
                detailResponse.setPartyFullname(itemlogDetail.getParty().getEmail());
                detailResponse.setPartyPhoneNumber(itemlogDetail.getParty().getPhoneNumber());
                detailResponse.setAddressInParty(itemlogDetail.getAddress());
                detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
                detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
                detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
                detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());
            }else{
                detailResponse.setSender(null);
                detailResponse.setReceiver(null);
                detailResponse.setPartyFullname(itemlogDetail.getParty().getEmail());
                detailResponse.setPartyPhoneNumber(itemlogDetail.getParty().getPhoneNumber());
                detailResponse.setAddressInParty(itemlogDetail.getAddress());
                detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
                detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
                detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
                detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());
            }

            return new ResponseEntity<>(detailResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }

    public ResponseEntity<?> editItemLog(EditItemLogDTO dataEditDTO) {
        /*
         * B1. Lay ra itemLogId can edit => Get ra thong tin
         * B2. Luu thong tin cua itemLogId do thanh 1 dong itemLogId khac
         * B3. Update thong tin cua ItemLogId trc do
         * */
        try {
            // B1: Lấy ra itemLogId cần edit => Get ra thông tin
            ItemLog itemLogDetail = itemLogRepository.findById(dataEditDTO.getItemLogId())
                    .orElseThrow(() -> new RuntimeException("ItemLog not found"));
            Item item = itemRepository.findById(itemLogDetail.getItem().getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            // B2: Lưu thông tin của itemLogId đó thành một dòng itemLogId khác
            ItemLog newItemLog = new ItemLog();
            copyItemLogDetails(newItemLog, itemLogDetail);
            newItemLog.setEvent_id(eventTypeRepository.findById(6)
                    .orElseThrow(() -> new RuntimeException("Event type not found"))); // Sự kiện chỉnh sửa
            itemLogRepository.save(newItemLog);

            // Kiểm tra quyền sở hữu
            if (itemService.checkOwner(dataEditDTO.getEmail(), item.getCurrentOwner())) {
                // B3: Cập nhật thông tin của ItemLogId trước đó
                updateLocation(dataEditDTO, itemLogDetail);
                updateAuthorized(dataEditDTO, itemLogDetail);

                if (hasNullFields(itemLogDetail)) {
                    String point = generateAndSetPoint(itemLogDetail);
                    long timestamp = System.currentTimeMillis();

                    // itemLogDetail.setIdEdit(true);
                    itemLogRepository.updateItemLog(dataEditDTO.getLocation().getAddress(), timestamp,
                            point, itemLogDetail.getItemLogId(), dataEditDTO.getDescription(), newItemLog.getItemLogId());

                    return ResponseEntity.status(HttpStatus.OK).body("Edit successfully.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! Unauthorized access.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return null;
    }

    private void copyItemLogDetails(ItemLog target, ItemLog source) {
        target.setAddress(source.getLocation().getAddress());
        target.setDescription(source.getDescription());
        target.setAuthorized(source.getAuthorized());
        target.setStatus(0);
        target.setTimeStamp(source.getTimeStamp());
        target.setItem(source.getItem());
        target.setLocation(source.getLocation());
        target.setParty(source.getParty());
    }

    private void updateLocation(EditItemLogDTO dataEditDTO, ItemLog itemLogDetail) {
        Location location = locationRepository.findOneByItemLogId(dataEditDTO.getItemLogId());
        locationRepository.updateLocation(location.getLocationId(),
                dataEditDTO.getLocation().getAddress(),
                dataEditDTO.getLocation().getCity(),
                dataEditDTO.getLocation().getCountry(),
                dataEditDTO.getLocation().getCoordinateX(),
                dataEditDTO.getLocation().getCoordinateY(),
                dataEditDTO.getLocation().getDistrict(),
                dataEditDTO.getLocation().getWard());
    }

    private void updateAuthorized(EditItemLogDTO dataEditDTO, ItemLog itemLogDetail) {
        Authorized authorized = authorizedRepository.findById(itemLogDetail.getAuthorized().getAuthorizedId())
                .orElseThrow(() -> new RuntimeException("Authorized not found"));
        authorizedRepository.updateAuthorized(dataEditDTO.getAuthorizedEmail(),
                dataEditDTO.getAuthorizedName(),
                authorized.getAuthorizedId());
    }

    private String generateAndSetPoint(ItemLog itemLogDetail) {
        double pointX = pointService.generateX();
        List<ItemLog> pointLogs = itemLogRepository.getPointItemId(itemLogDetail.getItem().getItemId());
        List<Point> pointList = pointService.getPointList(pointLogs);
        double pointY = pointService.lagrangeInterpolate(pointList, pointX);
        Point point = new Point(pointX, pointY);
        // itemLogDetail.setPoint(point.toString());
        //  itemLogDetail.setTimeStamp(System.currentTimeMillis());
        return point.toString();
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

    public ResponseEntity<?> editTransport(EditItemLogDTO dataEditDTO) {
        ItemLog itemlogDetail = itemLogRepository.findById(dataEditDTO.getItemLogId())
                .orElseThrow(() -> new RuntimeException("ItemLog not found"));
        Item item = itemRepository.getReferenceById(itemlogDetail.getItem().getItemId());

        if (itemService.checkOwner(dataEditDTO.getEmail(), item.getCurrentOwner())) {
            Location location = locationRepository.findOneByItemLogId(dataEditDTO.getItemLogId());
            locationRepository.updateLocation(location.getLocationId(), dataEditDTO.getLocation().getAddress(), dataEditDTO.getLocation().getCity(),
                    dataEditDTO.getLocation().getCountry(), dataEditDTO.getLocation().getCoordinateX(), dataEditDTO.getLocation().getCoordinateY(),
                    dataEditDTO.getLocation().getDistrict(), dataEditDTO.getLocation().getWard());

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
        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(itemId);
        return itemLogOptional.<ResponseEntity<?>>map(itemLog -> ResponseEntity.ok(itemLog.getEvent_id().getEventId())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ItemLogs found for itemId: " + itemId));
    }
    public ResponseEntity<?> getLocationItemId(int itemId) {
        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(itemId);
        return itemLogOptional.<ResponseEntity<?>>map(itemLog -> ResponseEntity.ok(itemLog.getLocation())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Location found for itemId: " + itemId));
    }

}
