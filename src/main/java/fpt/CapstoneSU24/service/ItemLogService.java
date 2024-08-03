package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.*;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.AuthorizedMapper;
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
    private final AuthorizedMapper authorizedMapper;
    private final ClientService clientService;

    @Autowired
    public ItemLogService(LocationRepository locationRepository,
                             ItemRepository itemRepository, PartyRepository partyRepository,
                             ItemLogRepository itemLogRepository,
                             EventTypeRepository eventTypeRepository, TransportRepository transportRepository,
                             AuthorizedRepository authorizedRepository, PointService pointService,
                          ItemService itemService, LogService logService, LocationMapper locationMapper,
                          ClientService clientService, AuthorizedMapper authorizedMapper) {
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
        this.authorizedMapper = authorizedMapper;
        this.clientService = clientService;
    }
    private static final Logger log = LoggerFactory.getLogger(ItemLogService.class);

    public ResponseEntity<?> getItemLogDetail(int itemLogId) {
        try {
            ItemLog itemlogDetail = itemLogRepository.getItemLogsById(itemLogId);
            if (itemlogDetail == null) {
                return new ResponseEntity<>("ItemLog not found.", HttpStatus.NOT_FOUND);
            }

            ItemLogDetailResponse detailResponse = new ItemLogDetailResponse();
            detailResponse.setItemLogId(itemlogDetail.getItemLogId());
            detailResponse.setEventType(itemlogDetail.getEvent_id() != null ? itemlogDetail.getEvent_id().getEvent_type() : null);

            if (itemlogDetail.getEvent_id() != null && itemlogDetail.getEvent_id().getEventId() == 3) {
                detailResponse.setSender(itemlogDetail.getAuthorized() != null ? itemlogDetail.getAuthorized().getAssignPersonMail() : null);
                detailResponse.setReceiver(itemlogDetail.getAuthorized() != null ? itemlogDetail.getAuthorized().getAuthorizedEmail() : null);
                detailResponse.setPartyFullname(itemlogDetail.getParty() != null ? itemlogDetail.getParty().getEmail() : null);
                detailResponse.setPartyPhoneNumber(itemlogDetail.getParty() != null ? itemlogDetail.getParty().getPhoneNumber() : null);
                detailResponse.setAddressInParty(itemlogDetail.getAddress());

                if (itemlogDetail.getLocation() != null) {
                    detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
                    detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
                } else {
                    detailResponse.setCoordinateX(null);
                    detailResponse.setCoordinateY(null);
                }

                detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
                detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());
                detailResponse.setIdEdit(itemlogDetail.getIdEdit());
                detailResponse.setCheckPoint(itemlogDetail.getPoint() != null);

            } else {
                detailResponse.setSender(null);
                detailResponse.setReceiver(null);
                detailResponse.setPartyFullname(itemlogDetail.getParty() != null ? itemlogDetail.getParty().getEmail() : null);
                detailResponse.setPartyPhoneNumber(itemlogDetail.getParty() != null ? itemlogDetail.getParty().getPhoneNumber() : null);
                detailResponse.setAddressInParty(itemlogDetail.getAddress());

                if (itemlogDetail.getLocation() != null) {
                    detailResponse.setCoordinateX(itemlogDetail.getLocation().getCoordinateX());
                    detailResponse.setCoordinateY(itemlogDetail.getLocation().getCoordinateY());
                } else {
                    detailResponse.setCoordinateX(null);
                    detailResponse.setCoordinateY(null);
                }

                detailResponse.setTimeReceive(itemlogDetail.getTimeStamp());
                detailResponse.setDescriptionItemLog(itemlogDetail.getDescription());
                detailResponse.setIdEdit(itemlogDetail.getIdEdit());
                detailResponse.setCheckPoint(itemlogDetail.getPoint() != null);

            }

            return new ResponseEntity<>(detailResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }

    public ResponseEntity<?> getItemLogDetailHistory(int itemLogId) {
        try {
            List<ItemLogDetailResponse> itemlogDetail = itemLogRepository.getListItemLogsByIdEdit(itemLogId);
            return new ResponseEntity<>(itemlogDetail, HttpStatus.OK);
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

            // Kiểm tra quyền sở hữu
            if (itemService.checkOwner(dataEditDTO.getEmail(), item.getCurrentOwner())) {
                int check = clientService.checkOTPinSQL2(dataEditDTO.getEmail().trim(), dataEditDTO.getOTP().trim(), item.getProductRecognition());
                if (check == 6)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! OTP is not correct.");
                if (check == 3 || check == 0) {
                    // B2: Lưu thông tin của itemLogId đó thành một dòng itemLogId khác
                    ItemLog newItemLog = new ItemLog();
                    copyItemLogDetails(newItemLog, itemLogDetail);
                    newItemLog.setTimeStamp(System.currentTimeMillis());
                    newItemLog.setIdEdit(itemLogDetail.getItemLogId());

                    newItemLog.setEvent_id(eventTypeRepository.findById(6)
                            .orElseThrow(() -> new RuntimeException("Event type not found"))); // Sự kiện chỉnh sửa
                    itemLogRepository.save(newItemLog);
                    String point = null;
                    // B3: Cập nhật thông tin của ItemLogId trước đó
                    Location savedLocation = locationRepository.save(locationMapper.locationDtoToLocation(dataEditDTO.getLocation()));
                    Authorized authorized = new Authorized();
                    authorized.setAuthorizedName(dataEditDTO.getAuthorizedName());
                    authorized.setAuthorizedEmail(dataEditDTO.getAuthorizedEmail());
                    authorized.setAssignPerson(itemLogDetail.getParty().getPartyFullName());
                    authorized.setAssignPerson(itemLogDetail.getParty().getEmail());
                    authorized.setDescription(dataEditDTO.getDescription());
                    authorized.setPhoneNumber(dataEditDTO.getPhoneNumber());
                    Authorized saveAuthorized = authorizedRepository.save(authorized);
                    if (!hasNullFields(savedLocation) && !hasNullFields(saveAuthorized)) {
                        point = generateAndSetPoint(itemLogDetail);
                    } else point = null;
                    itemLogRepository.updateItemLog(savedLocation.getAddress(), savedLocation.getLocationId(), saveAuthorized.getAuthorizedId(),
                            point, itemLogDetail.getItemLogId(), dataEditDTO.getDescription(), -1);

                    return ResponseEntity.status(HttpStatus.OK).body("Edit successfully.");
//                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! Unauthorized access.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return null;
    }

    private Party addParty(ItemLog itemLogDetail) {
        return partyRepository.save(new Party(
                itemLogDetail.getParty().getPartyFullName(),
                itemLogDetail.getDescription(), itemLogDetail.getParty().getPhoneNumber(),
                itemLogDetail.getParty().getEmail()));
    }

    public ResponseEntity<?> editItemLogByParty(EditItemLogPartyDTO dataEditDTO) {
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

            // Kiểm tra quyền sở hữu
            int check = clientService.checkOTPinSQL2(dataEditDTO.getEmail().trim(), dataEditDTO.getOTP().trim(), item.getProductRecognition());
            if (check == 6)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! OTP is not correct.");
            if (check == 4 || check == 0) {
                // B2: Lưu thông tin của itemLogId đó thành một dòng itemLogId khác
                ItemLog newItemLog = new ItemLog();
                copyItemLogDetails(newItemLog, itemLogDetail);
                newItemLog.setTimeStamp(System.currentTimeMillis());
                newItemLog.setIdEdit(itemLogDetail.getItemLogId());

                newItemLog.setEvent_id(eventTypeRepository.findById(6)
                        .orElseThrow(() -> new RuntimeException("Event type not found"))); // Sự kiện chỉnh sửa
                itemLogRepository.save(newItemLog);
                // B3: Cập nhật thông tin của ItemLogId trước đó
                Location savedLocation = locationRepository.save(dataEditDTO.getLocation());
                String point;
                if (!hasNullFields(itemLogDetail)) {
                    point = generateAndSetPoint(itemLogDetail);
                    itemLogRepository.updateItemLogByParty(dataEditDTO.getLocation().getAddress(), savedLocation.getLocationId(),
                            point, itemLogDetail.getItemLogId(), dataEditDTO.getDescription(), -1);
                } else point = null;
                itemLogRepository.updateItemLogByParty(dataEditDTO.getLocation().getAddress(), savedLocation.getLocationId(),
                        point, itemLogDetail.getItemLogId(), dataEditDTO.getDescription(), -1);
                return ResponseEntity.status(HttpStatus.OK).body("Edit successfully.");

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! Unauthorized access.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    public ResponseEntity<?> updateItemLog(UpdateItemLogDTO dataEditDTO) {
        /*
         * B1. Lay ra itemLogId can edit => Get ra thong tin
         * B2. Luu thong tin cua itemLogId do thanh 1 dong itemLogId khac
         * B3. Update thong tin cua ItemLogId trc do
         * */
        try {
//            // B1: Lấy ra itemLogId cần edit => Get ra thông tin
//            ItemLog itemLogDetail = itemLogRepository.findById(dataEditDTO.getItemLogId())
//                    .orElseThrow(() -> new RuntimeException("ItemLog not found"));
//            Item item = itemRepository.findById(itemLogDetail.getItem().getItemId())
//                    .orElseThrow(() -> new RuntimeException("Item not found"));
            Item item = itemRepository.findByProductRecognition(dataEditDTO.getProductRecognition());
            List<ItemLog> itemLogs = itemLogRepository.getItemLogsByItemIdIgnoreEdit(item.getItemId());
            ItemLog itemLogDetail = itemLogs.get(0);
            // Kiểm tra quyền sở hữu
            int check = clientService.checkOTPinSQL2(dataEditDTO.getEmail().trim(), dataEditDTO.getOTP().trim(), item.getProductRecognition());
            if (check == 6)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! OTP is not correct.");
            if (check == 3 || check == 0) {
                // B3: Cập nhật thông tin của ItemLogId trước đó
                Location savedLocation = locationRepository.save(dataEditDTO.getLocation());
                if (!hasNullFields(savedLocation)) {
                    String point = generateAndSetPoint(itemLogDetail);
                    itemLogRepository.updateItemLogLocation(savedLocation.getLocationId(), point, dataEditDTO.getLocation().getAddress()
                            , itemLogDetail.getItemLogId());

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

    public ResponseEntity<?> addItemLogTransport(EventItemLogDTO itemLogDTO) {
        try {
            // Retrieve item by product recognition
            Item item = itemRepository.findByProductRecognition(itemLogDTO.getProductRecognition());
            if (item == null) {
                return new ResponseEntity<>("Item not found.", HttpStatus.NOT_FOUND);
            }
            int check = clientService.checkOTPinSQL2(itemLogDTO.getEmailParty().trim(), itemLogDTO.getOTP().trim(), item.getProductRecognition());
            if (check == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has been cancelled!");
            if (check == 6)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! OTP time expire.");
            if (check == 3) {
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
                itemLog.setIdEdit(0);
                if (!hasNullFields(itemLog)) {
                    double pointX = pointService.generateX();
                    List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                    List<Point> pointList = pointService.getPointList(pointLogs);
                    double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                    Point point = new Point(pointX, pointY);
                    itemLog.setPoint(point.toString());
                }

                itemLogRepository.save(itemLog);
                return new ResponseEntity<>("Add successfully.", HttpStatus.OK);

            }
            return new ResponseEntity<>("OTP is not correct.", HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            logService.logError(ex);
            return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> editTransport(EventItemLogDTO dataEditDTO) {
        ItemLog itemLogDetail = itemLogRepository.findById(dataEditDTO.getItemLogId())
                .orElseThrow(() -> new RuntimeException("ItemLog not found"));
        Item item = itemRepository.getReferenceById(itemLogDetail.getItem().getItemId());

        if (itemService.checkOwner(dataEditDTO.getEmailParty(), item.getCurrentOwner())) {
//            boolean check = clientService.checkOTPinSQL(dataEditDTO.getEmailParty().trim(), dataEditDTO.getOTP().trim());
//            if (check) {
            int check = clientService.checkOTPinSQL2(dataEditDTO.getEmailParty().trim(), dataEditDTO.getOTP().trim(), item.getProductRecognition());
            if (check == 6)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! OTP is not correct.");
            if (check == 3 || check == 0) {
                // B2: Lưu thông tin của itemLogId đó thành một dòng itemLogId khác
                ItemLog newItemLog = new ItemLog();
                copyItemLogDetails(newItemLog, itemLogDetail);
                newItemLog.setIdEdit(itemLogDetail.getItemLogId());
                newItemLog.setEvent_id(eventTypeRepository.findById(6)
                        .orElseThrow(() -> new RuntimeException("Event type not found"))); // Sự kiện chỉnh sửa
                itemLogRepository.save(newItemLog);
                Party party = new Party();
                Transport transport = transportRepository.getReferenceById(dataEditDTO.getTransportId());
                party.setEmail(transport.getTransportEmail());
                party.setPartyFullName(transport.getTransportName());
                party.setPhoneNumber(transport.getTransportContact());
                party.setDescription(dataEditDTO.getDescriptionItemLog());
                Party savedParty = partyRepository.save(party);
                log.info("transport" + transport.getTransportId());
                if (!hasNullFields(party)) {
                    String point = generateAndSetPoint(itemLogDetail);
                    itemLogRepository.updateItemLogTransport(
                            point, dataEditDTO.getDescriptionItemLog(), -1,
                            savedParty.getPartyId(), itemLogDetail.getItemLogId());

                    return ResponseEntity.status(HttpStatus.OK).body("Edit successfully.");
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail! Unauthorized access.");

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edit fail!");
    }

    private void copyItemLogDetails(ItemLog target, ItemLog source) {
        target.setAddress(source.getLocation().getAddress());
        target.setDescription(source.getDescription());
        target.setAuthorized(source.getAuthorized());
        target.setStatus(source.getStatus());
        target.setTimeStamp(source.getTimeStamp());
        target.setItem(source.getItem());
        target.setLocation(source.getLocation());
        target.setParty(source.getParty());

    }

    //    private Location addLocation(Location location) {
//        return locationRepository.save(new Location(
//                        location.getAddress(),
//                location.getCity(),
//                location.getCountry(),
//                location.getCoordinateX(),
//                location.getCoordinateY(),
//                location.getDistrict(),
//                location.getWard()));
//    }
    private Authorized addAuthorized(ItemLog itemLogDetail) {
        Authorized authorized = new Authorized();
        authorized.setAuthorizedName(itemLogDetail.getAuthorized().getAuthorizedName());
        authorized.setAuthorizedEmail(itemLogDetail.getAuthorized().getAuthorizedEmail());
        return authorizedRepository.save(authorized);
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

    public static boolean hasNullFields(Object obj) {
        if (obj == null) {
            return true; // Nếu object là null, trả về true
        }

        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Cho phép truy cập các trường private
                if (field.get(obj) == null) {
                    return true; // Nếu có bất kỳ trường nào null, trả về true
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field: " + e.getMessage(), e);
        }

        return false; // Không có trường nào null
    }
    public ResponseEntity<?> getEventByItemId(int itemId) {
        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(itemId);
        return itemLogOptional.<ResponseEntity<?>>map(itemLog -> ResponseEntity.ok(itemLog.getEvent_id().getEventId())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ItemLogs found for itemId: " + itemId));
    }
    public ResponseEntity<?> getLocationItemId(int itemId) {
        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(itemId);
        return itemLogOptional.<ResponseEntity<?>>map(itemLog -> ResponseEntity.ok(itemLog.getLocation())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Location found for itemId: " + itemId));
    }


}
