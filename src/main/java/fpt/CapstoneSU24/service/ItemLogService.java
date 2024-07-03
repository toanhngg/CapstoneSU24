package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.EventItemLogDTO;
import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.dto.Point;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

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

    @Autowired
    public ItemLogService(LocationRepository locationRepository,
                             ItemRepository itemRepository, PartyRepository partyRepository,
                             ItemLogRepository itemLogRepository,
                             EventTypeRepository eventTypeRepository, TransportRepository transportRepository,
                             AuthorizedRepository authorizedRepository, PointService pointService,
                             ItemService itemService,LogService logService) {
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
    }

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
            itemLog.setAddress(itemLogDTO.getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
            itemLog.setAuthorized(authorized);
            itemLog.setStatus(0);
            itemLog.setTimeStamp(System.currentTimeMillis());
            itemLog.setItem(item);
            itemLog.setLocation(savedLocation);
            itemLog.setParty(savedParty);
            itemLog.setEvent_id(eventTypeRepository.findOneByEventId(itemLogDTO.getEventId()));

            // Additional validation
            if (itemLogDTO.getAddress() != null && !itemLogDTO.getAddress().isEmpty() &&
                    itemLogDTO.getDescriptionItemLog() != null && !itemLogDTO.getDescriptionItemLog().isEmpty() &&
                    itemLogDTO.getCity() != null && !itemLogDTO.getCity().isEmpty() &&
                    itemLogDTO.getCountry() != null && !itemLogDTO.getCountry().isEmpty() &&
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
