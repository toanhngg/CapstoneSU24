package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.controller.ItemController;
import fpt.CapstoneSU24.dto.*;
import fpt.CapstoneSU24.dto.payload.FilterByTimeStampRequest;
import fpt.CapstoneSU24.dto.payload.FilterSearchItemRequest;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.ItemMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.util.Const;
import fpt.CapstoneSU24.util.DocumentGenerator;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final LocationRepository locationRepository;
    private final ItemRepository itemRepository;
    private final PartyRepository partyRepository;
    private final ItemLogRepository itemLogRepository;
    private final QRCodeGenerator qrCodeGenerator;
    private final ProductRepository productRepository;
    private final ImageProductRepository imageProductRepository;
    private final ClientService clientService;
    private final AuthorizedRepository authorizedRepository;
    private final EventTypeRepository eventTypeRepository;
    private final ExportExcelService exportExcelService;
    private final UserRepository userRepository;
    private final PointService pointService;
    private final SpringTemplateEngine templateEngine;
    private final DocumentGenerator documentGenerator;
    private final OriginRepository originRepository;
    private final CloudinaryService cloudinaryService;
    private final LogService logService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(LocationRepository locationRepository, ProductRepository productRepository,
                       OriginRepository originRepository, ItemRepository itemRepository,
                       PartyRepository partyRepository, ItemLogRepository itemLogRepository,
                       QRCodeGenerator qrCodeGenerator, ImageProductRepository imageProductRepository,
                       ClientService clientService, AuthorizedRepository authorizedRepository,
                       EventTypeRepository eventTypeRepository, ExportExcelService exportExcelService,
                       UserRepository userRepository, PointService pointService, SpringTemplateEngine templateEngine,
                       DocumentGenerator documentGenerator, CloudinaryService cloudinaryService, LogService logService, ItemMapper itemMapper) {
        this.locationRepository = locationRepository;
        this.itemRepository = itemRepository;
        this.partyRepository = partyRepository;
        this.itemLogRepository = itemLogRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.imageProductRepository = imageProductRepository;
        this.clientService = clientService;
        this.authorizedRepository = authorizedRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.exportExcelService = exportExcelService;
        this.userRepository = userRepository;
        this.templateEngine = templateEngine;
        this.documentGenerator = documentGenerator;
        this.pointService = pointService;
        this.productRepository = productRepository;
        this.originRepository = originRepository;
        this.cloudinaryService = cloudinaryService;
        this.logService = logService;
        this.itemMapper = itemMapper;
    }
    /*
     * type is sort type: "desc" or "asc"
     * default data startDate and endDate equal 0 (need insert 2 data)
     * */
    public ResponseEntity<?> searchItem(FilterSearchItemRequest req) {
        try {
            Page<Item> items;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
//        Page<Item> items = jsonReq.getString("type") == null? itemRepository.findAll(pageable) : jsonReq.getString("type").equals("desc") ? itemRepository.sortItemsByCreatedAtDesc(pageable) :  itemRepository.sortItemsByCreatedAtAsc(pageable);
            if(req.getStartDate() != 0 && req.getEndDate() != 0){
                items = itemRepository.findAllItemWithDate(req.getProductId(), "%"+req.getName()+"%", "%"+req.getProductRecognition()+"%",req.getStartDate(), req.getEndDate(), pageable);
            }else{
                items = itemRepository.findAllItem(req.getProductId(), "%"+req.getName()+"%", "%"+req.getProductRecognition()+"%", pageable);
            }
            return ResponseEntity.status(200).body(items.map(itemMapper::itemToItemViewDTOResponse));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }

    public ResponseEntity<?> exportListItem(FilterByTimeStampRequest req) throws IOException {
        if (req.isValidDates()) {
            //  JSONObject jsonReq = new JSONObject(req);
            List<Item> items = itemRepository.findByCreatedAtBetween(req.getStartDate(), req.getEndDate());
            byte[] excelBytes = exportExcelService.exportItemToExcel(items);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported_data.xlsx");
            return new ResponseEntity<>(excelBytes, headers, 200);
        } else {
            return ResponseEntity.status(500).body("startTime need less than endTime");
        }
    }

    public ResponseEntity<?> addItem(ItemLogDTO itemLogDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            if (currentUser.getRole().getRoleId() == 2) {
                return handleAddItem(itemLogDTO, currentUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private ResponseEntity<?> handleAddItem(ItemLogDTO itemLogDTO, User currentUser) {
        try {
            User user = userRepository.getReferenceById(currentUser.getUserId());

            // Lưu địa chỉ
            Location location = createLocation(itemLogDTO);
            validateAndSetCoordinates(location, itemLogDTO);
            Location savedLocation = locationRepository.save(location);

            Origin saveOrigin = createAndSaveOrigin(itemLogDTO, user, savedLocation);

            List<Item> items = createItems(itemLogDTO, user, saveOrigin);
            List<Party> parties = createParties(itemLogDTO, user);
            List<ItemLog> itemLogs = createItemLogs(itemLogDTO, user, savedLocation, items, parties);

            // Save all entities in batch
            itemRepository.saveAll(items);
            partyRepository.saveAll(parties);
            itemLogRepository.saveAll(itemLogs);
            logService.info("Add successfully!");
            return ResponseEntity.status(HttpStatus.OK).body("Add successfully!");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private Location createLocation(ItemLogDTO itemLogDTO) {
        Location location = new Location();
        location.setAddress(itemLogDTO.getAddress());
        location.setCity(itemLogDTO.getCity());
        location.setCountry(itemLogDTO.getCountry());
        location.setDistrict(itemLogDTO.getDistrict());
        location.setWard(itemLogDTO.getStreet());
        return location;
    }

    private void validateAndSetCoordinates(Location location, ItemLogDTO itemLogDTO) throws ItemController.InvalidCoordinateException, ItemController.CoordinateOutOfRangeException {
        double corX = itemLogDTO.getCoordinateX();
        double corY = itemLogDTO.getCoordinateY();

        // Check if corX and corY are within their valid ranges
        if ((-90.0 <= corX && corX <= 90.0) && (-180.0 <= corY && corY <= 180.0)) {
            location.setCoordinateX(itemLogDTO.getCoordinateX());
            location.setCoordinateY(itemLogDTO.getCoordinateY());
            System.out.println("Coordinates set successfully.");
        } else {
            throw new ItemController.CoordinateOutOfRangeException("Coordinates are out of valid range.");
        }
    }

    private Origin createAndSaveOrigin(ItemLogDTO itemLogDTO, User user, Location savedLocation) throws NoSuchAlgorithmException {
        long scoreTime = System.currentTimeMillis();
        Origin origin = new Origin();
        origin.setCreatedAt(scoreTime);
        origin.setDescription(itemLogDTO.getDescriptionOrigin());
        origin.setSupportingDocuments(qrCodeGenerator.generateProductDescription(itemLogDTO.getProductId(), itemLogDTO.getAddress(), scoreTime));
        origin.setEmail(user.getEmail());
        origin.setFullNameManufacturer(user.getFirstName() + " " + user.getLastName());
        origin.setOrg_name(user.getOrg_name());
        origin.setPhone(user.getPhone());
        origin.setLocation(savedLocation);
        return originRepository.save(origin);
    }

    private List<Item> createItems(ItemLogDTO itemLogDTO, User user, Origin saveOrigin) throws IOException {

        List<Item> items = new ArrayList<>(itemLogDTO.getQuantity());
        long scoreTime = System.currentTimeMillis();
        for (int i = 0; i < itemLogDTO.getQuantity(); i++) {
            Item item = new Item();
            item.setCreatedAt(scoreTime);
            item.setCurrentOwner(user.getEmail());
            String productRecog = qrCodeGenerator.generateProductCode(itemLogDTO.getProductId());
            item.setProductRecognition(productRecog);
            item.setStatus(1);
            item.setOrigin(saveOrigin);
            item.setProduct(productRepository.findOneByProductId(itemLogDTO.getProductId()));
            Context context = new Context();

            String html = templateEngine.process(Const.TEMPLATE_FILE_NAME.CERTIFICATE, context);
            byte[] cert = documentGenerator.generatePdfFromHtml(html);
            String certLink = cloudinaryService.uploadPdfToCloudinary(cert, "trace_origin_cert_of+" + productRecog);
            item.setCertificateLink(certLink);
            items.add(item);
        }
        return items;
    }

    private List<Party> createParties(ItemLogDTO itemLogDTO, User user) {
        List<Party> parties = new ArrayList<>(itemLogDTO.getQuantity());
        for (int i = 0; i < itemLogDTO.getQuantity(); i++) {
            Party party = new Party();
            party.setDescription(itemLogDTO.getDescriptionOrigin());
            party.setEmail(user.getEmail());
            party.setPartyFullName(user.getFirstName() + " " + user.getLastName());
            party.setPhoneNumber(user.getPhone());
            parties.add(party);
        }
        return parties;
    }

    private List<ItemLog> createItemLogs(ItemLogDTO itemLogDTO, User user, Location savedLocation, List<Item> items, List<Party> parties) {
        List<ItemLog> itemLogs = new ArrayList<>(itemLogDTO.getQuantity());
        long scoreTime = System.currentTimeMillis();
        for (int i = 0; i < itemLogDTO.getQuantity(); i++) {
            ItemLog itemLog = new ItemLog();
            itemLog.setAddress(itemLogDTO.getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionOrigin());
            itemLog.setEvent_id(eventTypeRepository.findOneByEventId(1)); // tạo mới
            itemLog.setStatus(1);
            itemLog.setTimeStamp(scoreTime);
            itemLog.setItem(items.get(i));
            itemLog.setLocation(savedLocation);
            itemLog.setParty(parties.get(i));

            Point point = pointService.randomPoint(10000,10000);
            itemLog.setPoint(point.toString());
            itemLogs.add(itemLog);
        }
        return itemLogs;
    }

    private double parseCoordinate(String coordinate) throws ItemController.InvalidCoordinateException {
        if (coordinate == null || coordinate.isEmpty()) {
            throw new ItemController.InvalidCoordinateException("Coordinate is null or empty");
        }
        try {
            return Double.parseDouble(coordinate);
        } catch (NumberFormatException e) {
            throw new ItemController.InvalidCoordinateException("Coordinate format is invalid: " + coordinate);
        }
    }

    public ResponseEntity<?> findByProductId(int productId) {
        List<Item> itemList = itemRepository.findByProductId(productId);
        if (itemList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not foumd!");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(itemList);

        }
    }

    public ResponseEntity<?> viewLineItem(String productRecognition) {
        try {
            Item item = itemRepository.findByProductRecognition(productRecognition);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
            }
            List<ItemLog> itemList = itemLogRepository.getItemLogsByItemIdAsc(item.getItemId());

            if (itemList.isEmpty()) {
                // Trả về chỉ thông tin của Item nếu không có ItemLog nào
                //  ItemDTO itemDTO = convertToItemDTO(item);
                return ResponseEntity.status(HttpStatus.OK).body(new ItemLogDTOResponse(/*itemDTO, */Collections.emptyList()));

            } else {
                //  ItemDTO itemDTO = convertToItemDTO(item);
                List<ItemLogDTOResponse> itemLogDTOs = itemList.stream()
                        .map(this::convertToItemLogDTO)
                        .collect(Collectors.toList());
                // Tạo đối tượng ItemLogResponse và trả về
                ItemLogResponse itemLogResponse = new ItemLogResponse(/*itemDTO,*/ itemLogDTOs);
                return ResponseEntity.status(HttpStatus.OK).body(itemLogResponse);
            }
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing viewLineItem:: " + ex.getMessage());
        }
    }

    private ItemLogDTOResponse convertToItemLogDTO(ItemLog itemLog) {
        ItemLogDTOResponse dto = new ItemLogDTOResponse();
        dto.setItemLogId(itemLog.getItemLogId());
        dto.setAddress(itemLog.getAddress());
        long timestamp = itemLog.getTimeStamp();
        dto.setCreatedAt(String.valueOf(timestamp));
        dto.setEventType(itemLog.getEvent_id().getEvent_type());
        dto.setPartyName(itemLog.getParty().getPartyFullName());
        dto.setDescription(itemLog.getDescription());
        return dto;
    }

    public ResponseEntity<?> viewOrigin(int itemLogId) {
        try {
            ItemLog itemLog = itemLogRepository.getItemLogs(itemLogId);
            if (itemLog == null) return new ResponseEntity<>("ItemLog not found.", HttpStatus.NOT_FOUND);

            OriginDTO originDTO = new OriginDTO();
            originDTO.setCreateAt(itemLog.getItem().getCreatedAt());
            originDTO.setProductName(itemLog.getItem().getProduct().getProductName());
            originDTO.setProductRecognition(itemLog.getItem().getProductRecognition());
            originDTO.setOrgName(itemLog.getItem().getOrigin().getOrg_name());
            originDTO.setPhone(itemLog.getItem().getOrigin().getPhone());
            originDTO.setCoordinateY(itemLog.getItem().getOrigin().getLocation().getCoordinateY());
            originDTO.setCoordinateX(itemLog.getItem().getOrigin().getLocation().getCoordinateY());
            originDTO.setAddress(itemLog.getItem().getOrigin().getLocation().getAddress());
            originDTO.setFullName(itemLog.getItem().getOrigin().getFullNameManufacturer());
            originDTO.setDescriptionProduct(itemLog.getItem().getProduct().getDescription());
            originDTO.setDescriptionOrigin(itemLog.getItem().getOrigin().getDescription());
            originDTO.setWarranty(itemLog.getItem().getProduct().getWarranty());
            // Integer productId = itemLog.getItem().getProduct().getProductId();
            ImageProduct imageProduct = imageProductRepository.findByproductId(itemLog.getItem().getProduct().getProductId());
            if (imageProduct == null) {
                originDTO.setImage(null);
            } else {
                String img = imageProduct.getFilePath();
                originDTO.setImage(img);
            }
            return new ResponseEntity<>(originDTO, HttpStatus.OK);
        } catch (Exception ex) {
            logService.logError(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> getCertificate(String req, String productRecognition) {
        JSONObject jsonReq = new JSONObject(req);
        String email = jsonReq.getString("email");
        Item item = itemRepository.findByProductRecognition(productRecognition);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing.");
        }

        if (!item.getCurrentOwner().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not the current owner.");
        }

        List<ItemLog> itemLogs = itemLogRepository.getItemLogsByItemId(item.getItemId());
        if (itemLogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insufficient item logs.");
        }

        List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
        if (pointLogs.size() != itemLogs.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mismatch in item logs and point logs.");
        }
        if (!pointService.arePointsOnCurve(pointLogs)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Points do not form a valid graph.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificate.pdf");

        String pdfData = item.getCertificateLink();
        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }

    public ResponseEntity<Boolean> confirmCurrentOwner(SendOTP otp, String productRecognition) {
        Item item = itemRepository.findByProductRecognition(productRecognition); // B1
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Nếu item không tồn tại
        }

        if (item.getStatus() == 1 && item.getCurrentOwner().equals(otp.getEmail())) {
            boolean check = clientService.checkOTPinSQL(otp.getEmail(), otp.getOtp());
            if (check) {
                return ResponseEntity.ok(true); // Thành công, trả về true
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // OTP không chính xác
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Status không phù hợp
        }
    }

    public ResponseEntity<Boolean> checkAuthorized(String productRecognition) {
        Item item = findByProductRecognition(productRecognition); // B1
        List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId()); // tim cai dau tien
        if (list.get(0).getAuthorized() == null) {
            return ResponseEntity.ok(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }

    public ResponseEntity<?> authorize(AuthorizedDTO authorized) {
        try {
            Item item = findByProductRecognition(authorized.getProductRecognition());
            // kiểm tra người đang ủy quyền có phải current owner không
            if (checkOwner(authorized.getAssign_person_mail(), item.getProductRecognition())) {
                if (item.getStatus() == 0) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This product has been authorized!");
                }
                // B2: Tiến hành ủy quyền nếu chưa ủy quyền
                if (addEventAuthorized(authorized, item)) {
                    // Gửi thông báo email
                    ClientSdi sdi = new ClientSdi();
                    sdi.setEmail(authorized.getAuthorized_email());
                    sdi.setUsername(authorized.getAuthorized_name());
                    sdi.setName(authorized.getAuthorized_name());
                    clientService.notification(sdi);
                    return ResponseEntity.status(HttpStatus.OK).body("Authorization successful!");
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner");
            }
            // B1: Kiểm tra trạng thái ủy quyền của sản phẩm

        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
        return null;
    }

    public ResponseEntity<Boolean> checkCurrentOwner(String email, String productRecognition) {
        logService.info("checkCurrentOwner"+ " " + email + " " + productRecognition);
        try{
            if (checkOwner(email, productRecognition)) {
                    return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            }
        }catch (Exception e){
            logService.logError(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    public boolean checkOwner(String email, String productRecognition) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Item item = itemRepository.findByProductRecognition(productRecognition);
        if (item == null) {
            return false;
        }
        return email.equals(item.getCurrentOwner());
    }

    public Item findByProductRecognition(String productRecognition) {
        if (productRecognition == null || productRecognition.isEmpty()) {
            throw new IllegalArgumentException("Product recognition cannot be null or empty");
        }
        return itemRepository.findByProductRecognition(productRecognition);
    }

    public ItemLog getItemLogs(int itemLogId) {
        return itemLogRepository.getItemLogs(itemLogId);
    }

    public boolean addEventAuthorized(AuthorizedDTO authorized, Item item) {
        if (authorized == null || item == null) {
            return false;
        }
        try {
            Location savedLocation = locationRepository.save(authorized.getLocation());
            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return false;
            }
            ItemLog itemIndex = list.get(0);

            Authorized authorizedSaved = authorizedRepository.save(new Authorized(
                    authorized.getAuthorized_name(),
                    authorized.getAuthorized_email(),
                    itemIndex.getParty().getPartyFullName(),
                    item.getCurrentOwner(),
                    savedLocation,
                    authorized.getPhoneNumber(),
                    authorized.getDescription()
            ));

            Point point = null;

            if (authorized.getLocation().getAddress() != null &&
                    authorized.getLocation().getCountry() != null &&
                    authorized.getLocation().getCoordinateX() != 0 &&
                    authorized.getLocation().getWard() != null &&
                    authorized.getLocation().getDistrict() != null &&
                    authorized.getLocation().getCity() != null &&
                    authorized.getLocation().getCoordinateY() != 0 &&
                    authorized.getDescription() != null) {

                double pointX = pointService.generateX();
                List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                List<Point> pointList = pointService.getPointList(pointLogs);
                double pointY = pointService.lagrangeInterpolate(pointList, pointX);

                point = new Point(pointX, pointY);
            }

            itemLogRepository.save(new ItemLog(
                    item,
                    authorized.getLocation().getAddress(),
                    itemIndex.getParty(),
                    itemIndex.getLocation(),
                    System.currentTimeMillis(),
                    authorized.getDescription(),
                    authorizedSaved,
                    new EventType(3),  // 3 indicates authorization event
                    1,
                    null,
                    point != null ? point.toString() : null
            ));

            return true;
        } catch (Exception ex) {
            logService.logError(ex);
            return false;
        }
    }

    public ResponseEntity<?> sendCurrentOwnerOTP(String req) {
        try {
            JSONObject jsonReq = new JSONObject(req);
            String email = jsonReq.getString("email");

            // Tạo đối tượng ClientSdi và gửi email OTP
            ClientSdi sdi = new ClientSdi();
            sdi.setEmail(email);

            boolean emailSent = clientService.createMailAndSaveSQL(sdi);

            if (emailSent) {
                return ResponseEntity.ok("OTP has been sent successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
            }
        } catch (JSONException e) {
            logService.logError(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON request.");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    public ResponseEntity<?> sendOTP(String req, String productRecognition) {
        try {
            JSONObject jsonReq = new JSONObject(req);
            String email = jsonReq.getString("email");
            // Kiểm tra xem item có tồn tại và có status = 0 hay không
            Item item = findByProductRecognition(productRecognition);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
            }
            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ItemLog not found!");
            }
            ItemLog itemIndex = list.get(0);
            if (itemIndex.getAuthorized() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has not been authorized!");
            }
            System.out.println(itemIndex.getAuthorized().getAuthorized_email());
            System.out.println(email);
            // Kiểm tra xem email có đúng là current owner không
            if (!(itemIndex.getAuthorized().getAuthorized_email()).equals(email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the current owner.");
            }
            // Tạo đối tượng ClientSdi và gửi email OTP
            ClientSdi sdi = new ClientSdi();
            sdi.setEmail(itemIndex.getAuthorized().getAuthorized_email());
            sdi.setUsername(itemIndex.getAuthorized().getAuthorized_name());
            sdi.setName(itemIndex.getAuthorized().getAuthorized_name());

            boolean emailSent = clientService.createMailAndSaveSQL(sdi);

            if (emailSent) {
                return ResponseEntity.ok("OTP has been sent successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
            }
        } catch (JSONException e) {
            logService.logError(e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON request.");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    public ResponseEntity<Boolean> confirmOTP(SendOTP otp, String productRecognition) {
        try {
            /**
             // B1: Người dùng nhập OTP confirm chính xác bằng cách check OTP trong DB và người dùng nhập
             // - Chính xác => Cập nhật status trong item CurrentOwner thành status là 1
             // - Insert bảng party => Sau khi nhận mới trở thành party
             */
            Item item = itemRepository.findByProductRecognition(productRecognition); // B1
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Nếu item không tồn tại
            }
            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }
            ItemLog itemIndex = list.get(0);
            if (itemIndex.getAuthorized() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            }

            // Kiểm tra xem email có đúng là current owner không
            if (!(itemIndex.getAuthorized().getAuthorized_email()).equals(otp.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
            }
            boolean check = clientService.checkOTPinSQL(otp.getEmail().trim(), otp.getOtp().trim());
                if (check) {
                    int status = 1;
                    itemRepository.updateItemStatusAndCurrentOwnwe((long) item.getItemId(), status, itemIndex.getAuthorized().getAuthorized_email());
                    Party party = partyRepository.save(new Party(
                            itemIndex.getAuthorized().getDescription(),
                            itemIndex.getAuthorized().getAuthorized_email(),
                            itemIndex.getAuthorized().getAuthorized_name(),
                            itemIndex.getAuthorized().getPhoneNumber()
                    ));

                    ItemLog itemLog = new ItemLog();
                    itemLog.setAddress(itemIndex.getAuthorized().getLocation().getAddress());
                    itemLog.setDescription(itemIndex.getAuthorized().getDescription());
                    itemLog.setAuthorized(null);
                    itemLog.setStatus(1);
                    itemLog.setTimeStamp(System.currentTimeMillis());
                    itemLog.setItem(item);
                    itemLog.setLocation(itemIndex.getAuthorized().getLocation());
                    itemLog.setParty(party);

                    double pointX = pointService.generateX();
                    List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                    List<Point> pointList = pointService.getPointList(pointLogs);
                    double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                    Point point = new Point(pointX, pointY);
                    itemLog.setPoint(point.toString());
                    itemLog.setEvent_id(eventTypeRepository.findOneByEventId(4)); // Event này là nhận hàng
                    itemLog.setImageItemLog(null);
                    itemLogRepository.updateStatus(1, list.get(1).getItemLogId());
                    itemLogRepository.save(itemLog);

                    return ResponseEntity.ok(true); // Thành công, trả về true
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // OTP không chính xác
                }
        } catch (Exception ex) {
            // Log the error
            // System.err.println("Error occurred while confirming OTP: " + ex.getMessage());
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Xảy ra lỗi, trả về lỗi server
        }
    }

    public ResponseEntity<String> abortItem(AbortDTO abortDTO, String productRecognition) {
        try{
            JSONObject jsonReq = new JSONObject(abortDTO.getItem().getCurrentOwner());
            String email = jsonReq.getString("email");
//            if (checkOwner(email, productRecognition)) {
//                // Ghi lai item log
//                ItemLog itemLog = new ItemLog();
//                itemLog.setAddress(abortDTO.getAddress());
//                itemLog.setDescription(abortDTO.getDescription());
//                itemLog.setAuthorized(null);
//                itemLog.setStatus(1);
//                itemLog.setTimeStamp(System.currentTimeMillis());
//                itemLog.setItem(abortDTO.getItem());
//                itemLog.setLocation(itemIndex.getAuthorized().getLocation());
//                itemLog.setParty(party);
//
//                double pointX = pointService.generateX();
//                List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
//                List<Point> pointList = pointService.getPointList(pointLogs);
//                double pointY = pointService.lagrangeInterpolate(pointList, pointX);
//                Point point = new Point(pointX, pointY);
//                itemLog.setPoint(point.toString());
//                itemLog.setEvent_id(eventTypeRepository.findOneByEventId(4)); // Event này là nhận hàng
//                itemLog.setImageItemLog(null);
//                itemLogRepository.updateStatus(1, list.get(1).getItemLogId());
//                itemLogRepository.save(itemLog);
//
//                return ResponseEntity.ok(true); // Thành công, trả về true
//                // Cap nhat trang thai cua item => da bi huy
//                itemRepository.updateItemStatus(productRecognition,5);
//                return ResponseEntity.status(HttpStatus.OK).body("Abort successfully!");
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not currentOwner!");
//            }
        }catch (Exception ex){
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
        return null;
    }


//    private ItemDTO convertToItemDTO(Item item) {
//        ItemDTO dto = new ItemDTO();
//        dto.setProductRecognition(item.getProductRecognition());
//        return dto;
//    }

//    public static class InvalidCoordinateException extends Exception {
//        public InvalidCoordinateException(String message) {
//            super(message);
//        }
//    }
//
//    public static class CoordinateOutOfRangeException extends Exception {
//        public CoordinateOutOfRangeException(String message) {
//            super(message);
//        }
//    }

}
