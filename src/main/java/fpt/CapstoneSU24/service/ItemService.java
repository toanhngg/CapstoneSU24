package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.*;
import fpt.CapstoneSU24.dto.CertificateInfor.InformationCert;
import fpt.CapstoneSU24.dto.payload.FilterByTimeStampRequest;
import fpt.CapstoneSU24.dto.payload.FilterSearchItemRequest;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.*;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.util.Const;
import fpt.CapstoneSU24.util.DocumentGenerator;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private final AbortMapper abortMapper;
    private final LocationMapper locationMapper;
    private final ProductMapper productMapper;
    private final AuthorizedMapper authorizedMapper;

    @Autowired
    public ItemService(LocationRepository locationRepository, ProductRepository productRepository,
                       OriginRepository originRepository, ItemRepository itemRepository,
                       PartyRepository partyRepository, ItemLogRepository itemLogRepository,
                       QRCodeGenerator qrCodeGenerator, ImageProductRepository imageProductRepository,
                       ClientService clientService, AuthorizedRepository authorizedRepository,
                       EventTypeRepository eventTypeRepository, ExportExcelService exportExcelService,
                       UserRepository userRepository, PointService pointService, SpringTemplateEngine templateEngine,
                       DocumentGenerator documentGenerator, CloudinaryService cloudinaryService, LogService logService,
                       AbortMapper abortMapper, ItemMapper itemMapper, LocationMapper locationMapper,
                       ProductMapper productMapper, AuthorizedMapper authorizedMapper) {
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
        this.abortMapper = abortMapper;
        this.locationMapper = locationMapper;
        this.productMapper = productMapper;
        this.authorizedMapper = authorizedMapper;
    }
    private static final Logger log = LoggerFactory.getLogger(ItemService.class);

    public ResponseEntity<?> searchItem(FilterSearchItemRequest req) {
        Page<Item> items;
        Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")) :
                req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt")) :
                        PageRequest.of(req.getPageNumber(), req.getPageSize());
        try {
            if (req.getEventTypeId() != 0) {
                if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                    items = itemRepository.findAllItemWithDate(req.getProductId(), "%" + req.getName() + "%", "%" + req.getProductRecognition() + "%", req.getStartDate(), req.getEndDate(), req.getEventTypeId(), pageable);
                } else {
                    items = itemRepository.findAllItem(req.getProductId(), "%" + req.getName() + "%", "%" + req.getProductRecognition() + "%", req.getEventTypeId(), pageable);
                }
            } else {
                if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                    items = itemRepository.findAllItemWithDate(req.getProductId(), "%" + req.getName() + "%", "%" + req.getProductRecognition() + "%", req.getStartDate(), req.getEndDate(), pageable);
                } else {
                    items = itemRepository.findAllItem(req.getProductId(), "%" + req.getName() + "%", "%" + req.getProductRecognition() + "%", pageable);
                }
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

    public ResponseEntity<?> handleAddItem(ItemLogDTO itemLogDTO, User currentUser) {
        try {
            User user = userRepository.getReferenceById(currentUser.getUserId());
            //   Location location = locationMapper.locationDtoToLocation(itemLogDTO.getLocation());
            Location savedLocation = locationRepository.save(locationMapper.locationDtoToLocation(itemLogDTO.getLocation()));

            Origin saveOrigin = createAndSaveOrigin(itemLogDTO, user, savedLocation);

            List<Item> items = createItems(itemLogDTO, user, saveOrigin);
            List<Party> parties = createParties(itemLogDTO, user);
            List<ItemLog> itemLogs = createItemLogs(itemLogDTO, user, savedLocation, items, parties);

            // Save all entities in batch
            itemRepository.saveAll(items);
            partyRepository.saveAll(parties);
            itemLogRepository.saveAll(itemLogs);
            return ResponseEntity.status(HttpStatus.OK).body("Add successfully!");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private Origin createAndSaveOrigin(ItemLogDTO itemLogDTO, User user, Location savedLocation) throws NoSuchAlgorithmException {
        long scoreTime = System.currentTimeMillis();
        Origin origin = new Origin();
        origin.setCreatedAt(scoreTime);
        origin.setDescription(itemLogDTO.getDescriptionOrigin());
        origin.setSupportingDocuments(qrCodeGenerator.generateProductDescription(itemLogDTO.getProductId(), itemLogDTO.getLocation().getAddress(), scoreTime));
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

            InformationCert informationCert = new InformationCert();
            Map<String, Object> props = new HashMap<>();
            props.put("orgName", user.getOrg_name());
            props.put("itemName", item.getProduct().getProductName());
            props.put("supportingDocuments", item.getOrigin().getSupportingDocuments());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = Instant.ofEpochMilli(scoreTime).atZone(ZoneId.systemDefault()).format(formatter);
            props.put("createAt", formattedDate);
            props.put("productRecognition", item.getProductRecognition());
            informationCert.setProps(props);

            Context context = new Context();
            context.setVariables(informationCert.getProps());
            String html = templateEngine.process(Const.TEMPLATE_FILE_NAME.CERTIFICATE, context);
            byte[] cert = documentGenerator.generatePdfFromHtml(html);
            // byte[] cert = documentGenerator.generateImageFromHtml(html);
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
            itemLog.setAddress(itemLogDTO.getLocation().getAddress());
            itemLog.setDescription(itemLogDTO.getDescriptionOrigin());
            itemLog.setEvent_id(eventTypeRepository.findOneByEventId(1)); // tạo mới
            itemLog.setStatus(1);
            itemLog.setTimeStamp(scoreTime);
            itemLog.setItem(items.get(i));
            itemLog.setLocation(savedLocation);
            itemLog.setParty(parties.get(i));

            Point point = pointService.randomPoint(10000, 10000);
            itemLog.setPoint(point.toString());
            itemLogs.add(itemLog);
        }
        return itemLogs;
    }

    public ResponseEntity<?> viewLineItem(String productRecognition) {
        try {
            log.info("itemviewLineItem");
            if (productRecognition.length() < 10) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please input string has 10 characters");
            }
            Item item = itemRepository.findByProductRecognition(productRecognition);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
            }
            //List<ItemLog> itemList = itemLogRepository.getItemLogsByItemIdAsc(item.getItemId());
            List<ItemLog> itemList = itemLogRepository.getItemLogsByItemIdAscNotEdit(item.getItemId());

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
            Page<Product> products = null;
            ItemLog itemLog = itemLogRepository.getItemLogs(itemLogId);
            if (itemLog == null) return new ResponseEntity<>("ItemLog not found.", HttpStatus.NOT_FOUND);

            OriginDTO originDTO = new OriginDTO();
            originDTO.setCreateAt(itemLog.getItem().getCreatedAt());
            originDTO.setProductName(itemLog.getItem().getProduct().getProductName());
            originDTO.setProductRecognition(itemLog.getItem().getProductRecognition());
            originDTO.setOrgName(itemLog.getItem().getOrigin().getOrg_name());
            originDTO.setPhone(itemLog.getItem().getOrigin().getPhone());
            originDTO.setLocationDTO(locationMapper.locationToLocationDto(itemLog.getItem().getOrigin().getLocation()));
            originDTO.setFullName(itemLog.getItem().getOrigin().getFullNameManufacturer());
            originDTO.setDescriptionProduct(itemLog.getItem().getProduct().getDescription());
            originDTO.setDescriptionOrigin(itemLog.getItem().getOrigin().getDescription());
            originDTO.setWarranty(itemLog.getItem().getProduct().getWarranty());
            int productId = itemLog.getItem().getProduct().getProductId();
            List<String> imageProducts = imageProductRepository.findAllFilePathNotStartingWithAvatar(productId)
                    .stream().map(cloudinaryService::getImageUrl).toList();
            originDTO.setImage(imageProducts);
            return new ResponseEntity<>(originDTO, HttpStatus.OK);
        } catch (Exception ex) {
            logService.logError(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> getCertificate(CurrentOwnerCheckDTO req) {
//        JSONObject jsonReq = new JSONObject(req);
//        String email = jsonReq.getString("email");
//        String productRecognition = jsonReq.getString("productRecognition");
        String email = req.getEmail();
        String productRecognition = req.getProductRecognition();
        Item item = itemRepository.findByProductRecognition(productRecognition);
        if (productRecognition == null || productRecognition.isEmpty()) {
            return ResponseEntity.badRequest().body("Product recognition is required");
        }
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing.");
        }

        if (!item.getCurrentOwner().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not the current owner.");
        }

        List<ItemLog> itemLogs = itemLogRepository.getItemLogsByItemIdIgnoreEdit(item.getItemId());
        if (itemLogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insufficient item logs.");
        }

        List<ItemLog> pointLogs = itemLogRepository.getPointItemIdIgnoreEdit(item.getItemId());
        if (pointLogs.size() != itemLogs.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mismatch in item logs and point logs.");
        }
        if (!pointService.arePointsOnCurve(pointLogs)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Points do not form a valid graph.");
        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "certificate.pdf");
//
//        String pdfData = item.getCertificateLink();
//        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
        String pdfData = item.getCertificateLink();
        return new ResponseEntity<>(pdfData, HttpStatus.OK);
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

//    public ResponseEntity<Boolean> checkEventAuthorized(String productRecognition) {
//        Item item = findByProductRecognition(productRecognition); // B1
//        if (item.getStatus() == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
//        List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId()); // tim cai dau tien
//        if (list.get(0).getAuthorized() == null) {
//            return ResponseEntity.ok(false);
//        } else {
//            return ResponseEntity.ok(true);
//        }
//    }

    public ResponseEntity<?> authorize(AuthorizedDTO authorized) {
        try {
            Item item = findByProductRecognition(authorized.getProductRecognition());
            // kiểm tra người đang ủy quyền có phải current owner không
            if (item.getStatus() == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has been cancelled!");
            if (checkOwner(authorized.getAssignPersonMail(), item.getCurrentOwner())) {
                if (!authorized.getAuthorizedEmail().equals(authorized.getAssignPersonMail())) {
                    return addEventAuthorized(authorized, item);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail authorized not same mail assign person");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner");
            }
            // B1: Kiểm tra trạng thái ủy quyền của sản phẩm

        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    //    public ResponseEntity<Boolean> checkCurrentOwner(CurrentOwnerCheckDTO req) {
//        JSONObject jsonReq = new JSONObject(req);
//        String email = jsonReq.getString("email");
//        String productRecognition = jsonReq.getString("productRecognition");
//
//        logService.info("checkCurrentOwner"+ " " + email + " " + productRecognition);
//        Item item = findByProductRecognition(productRecognition);
//        if (item.getStatus() == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
//        try{
//            if (checkOwner(email, item.getCurrentOwner())) {
//                    return ResponseEntity.ok(true);
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
//            }
//        }catch (Exception e){
//            logService.logError(e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
//        }
//    }
//
    public ResponseEntity<Integer> check(CurrentOwnerCheckDTO req) {
//        JSONObject jsonReq = new JSONObject(req);
//        String email = jsonReq.getString("email");
//        String productRecognition = jsonReq.getString("productRecognition");
        String email = req.getEmail();
        String productRecognition = req.getProductRecognition();
        Item item = findByProductRecognition(productRecognition);
        List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId()); // tim cai dau tien
//        logService.info("checkCurrentOwner"+ " " + email + " " + productRecognition);

        if (item.getStatus() == 0) ResponseEntity.ok(0);
        try {
            if (checkOwner(email, item.getCurrentOwner())) {
                return ResponseEntity.ok(1); // is CurrentOwner
            }
            if (list.get(0).getAuthorized().getAuthorizedEmail().equals(req.getEmail())) {
                return ResponseEntity.ok(2); // is Authorized
            }
        } catch (Exception e) {
            logService.logError(e);
            return ResponseEntity.ok(3); // Exception
        }
        return ResponseEntity.ok(4);
    }

    public boolean checkOwner(String email, String emailCurrentOwner) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (emailCurrentOwner == null || emailCurrentOwner.isEmpty()) {
            return false;
        }
        return email.equals(emailCurrentOwner);
    }

    public Item findByProductRecognition(String productRecognition) {
        if (productRecognition == null || productRecognition.isEmpty()) {
            throw new IllegalArgumentException("Product recognition cannot be null or empty");
        }
        return itemRepository.findByProductRecognition(productRecognition);
    }

//    public ItemLog getItemLogs(int itemLogId) {
//        return itemLogRepository.getItemLogs(itemLogId);
//    }

    public ResponseEntity<String> addEventAuthorized(AuthorizedDTO authorized, Item item) {
        if (authorized == null || item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorized or Item is null");
        }

        try {
            // Reuse HttpClient instance if used elsewhere
            HttpClient client = HttpClient.newHttpClient();
            String email = authorized.getAuthorizedEmail();

            // Validate email
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://melink.vn/checkmail/checkemail.php"))
                    .POST(HttpRequest.BodyPublishers.ofString("email=" + email))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (!("<span style='color:green'><b>Valid!</b>").equals(responseBody)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not exist!");
            }

            Location savedLocation = locationRepository.save(locationMapper.locationDtoToLocation(authorized.getLocation()));
            List<ItemLog> itemLogs = itemLogRepository.getItemLogsByItemId(item.getItemId());

            if (itemLogs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item logs not found");
            }

            ItemLog itemIndex = itemLogs.get(0);
            if (itemIndex.getEvent_id().getEventId() == 3)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The item is in an authorized state and cannot be authorized again");
            else if (itemIndex.getEvent_id().getEventId() == 2)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The product is in delivery status");

            long timeInsert = System.currentTimeMillis();
            long timeDifference = timeInsert - itemIndex.getTimeStamp();

            if (timeDifference > TimeUnit.DAYS.toMillis(3)) {
                Authorized authorizedEntity = authorizedMapper.authorizedDtoToAuthorized(authorized);
                authorizedEntity.setLocation(savedLocation);
                Authorized authorizedSaved = authorizedRepository.save(authorizedEntity);

                Point point = null;

                if (isLocationValid(authorized.getLocation())) {
                    double pointX = pointService.generateX();
                    List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                    List<Point> pointList = pointService.getPointList(pointLogs);
                    double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                    point = new Point(pointX, pointY);
                }

                itemLogRepository.save(new ItemLog(
                        item,
                        itemIndex.getAddress(),
                        itemIndex.getParty(),
                        itemIndex.getLocation(),
                        timeInsert,
                        authorized.getDescription(),
                        authorizedSaved,
                        new EventType(3),
                        1,
                        null,
                        point != null ? point.toString() : null
                ));

                // Send email notification
                ClientSdi sdi = new ClientSdi();
                sdi.setEmail(authorized.getAuthorizedEmail());
                sdi.setUsername(authorized.getAuthorizedName());
                sdi.setName(authorized.getAuthorizedName());
                clientService.notification(sdi);

                return ResponseEntity.status(HttpStatus.OK).body("Authorization successful!");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot authorize product at this time!");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    private boolean isLocationValid(LocationDTO location) {
        return location.getAddress() != null &&
                location.getCountry() != null &&
                location.getCoordinateX() != 0 &&
                location.getWard() != null &&
                location.getDistrict() != null &&
                location.getCity() != null &&
                location.getCoordinateY() != 0;
    }

    public ResponseEntity<?> sendCurrentOwnerOTP(CurrentOwnerCheckDTO req) {
        try {
            String email = req.getEmail();
            String productRecognition = req.getProductRecognition();
//            JSONObject jsonReq = new JSONObject(req);
//            String email = jsonReq.getString("email");
//            String productRecognition = jsonReq.getString("productRecognition");

            // Kiểm tra xem item có tồn tại và có status = 0 hay không
            Item item = findByProductRecognition(productRecognition);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
            }
            if (item.getStatus() == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has been cancelled!");
            if (checkOwner(email, item.getCurrentOwner())) {
                // Tạo đối tượng ClientSdi và gửi email OTP
                ClientSdi sdi = new ClientSdi();
                sdi.setEmail(item.getCurrentOwner());
                sdi.setUsername(item.getCurrentOwner());
                sdi.setName(item.getCurrentOwner());
                boolean emailSent = clientService.createMailAndSaveSQL(sdi);

                if (emailSent) {
                    return ResponseEntity.ok("OTP has been sent successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
                }
            }

        } catch (JSONException e) {
            logService.logError(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON request.");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
        return null;
    }

    public ResponseEntity<?> sendOTP(CurrentOwnerCheckDTO req) {
        try {
            String email = req.getEmail();
            String productRecognition = req.getProductRecognition();
//            JSONObject jsonReq = new JSONObject(req);
//            String email = jsonReq.getString("email");
//            String productRecognition = jsonReq.getString("productRecognition");

            // Kiểm tra xem item có tồn tại và có status = 0 hay không
            Item item = findByProductRecognition(productRecognition);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
            }
            if (item.getStatus() == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ItemLog not found!");
            }
            ItemLog itemIndex = list.get(0);
            if (itemIndex.getAuthorized() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has not been authorized!");
            }
            // System.out.println(itemIndex.getAuthorized().getAuthorizedEmail());
            //System.out.println(email);
            // Kiểm tra xem email có đúng là current owner không
            if (!(itemIndex.getAuthorized().getAuthorizedEmail()).equals(email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the current owner.");
            }
            // Tạo đối tượng ClientSdi và gửi email OTP
            ClientSdi sdi = new ClientSdi();
            sdi.setEmail(itemIndex.getAuthorized().getAuthorizedEmail());
            sdi.setUsername(itemIndex.getAuthorized().getAuthorizedName());
            sdi.setName(itemIndex.getAuthorized().getAuthorizedName());

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
            if (item.getStatus() == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }
            ItemLog itemIndex = list.get(0);
            if (itemIndex.getAuthorized() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            }

            // Kiểm tra xem email có đúng là current owner không
            if (!(itemIndex.getAuthorized().getAuthorizedEmail()).equals(otp.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
            }
            boolean check = clientService.checkOTPinSQL(otp.getEmail().trim(), otp.getOtp().trim());
            if (check) {
                int status = 1;
                itemRepository.updateItemStatusAndCurrentOwnwe((long) item.getItemId(), status, itemIndex.getAuthorized().getAuthorizedEmail());
                Party party = partyRepository.save(new Party(
                        itemIndex.getAuthorized().getAuthorizedName(),
                        itemIndex.getAuthorized().getDescription(),
                        itemIndex.getAuthorized().getPhoneNumber(),
                        itemIndex.getAuthorized().getAuthorizedEmail()
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
                // Check if there are at least two elements in the list before accessing index 1
                if (list.size() > 1) {
                    itemLogRepository.updateStatus(1, list.get(1).getItemLogId());
                }
                itemLogRepository.save(itemLog);
                return ResponseEntity.ok(true); // Thành công, trả về true
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // OTP không chính xác
            }
        } catch (Exception ex) {
            logService.logError(ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(false); // Xảy ra lỗi, trả về lỗi server
        }
    }


    public ResponseEntity<String> abortItem(AbortDTO abortDTO) {
        try {
            Item item = itemRepository.findByProductRecognition(abortDTO.getProductRecognition());
            long timeInsert = System.currentTimeMillis();
            List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("List not found!");
            }
            ItemLog itemIndex = list.get(0);
            long timeDifference = timeInsert - itemIndex.getTimeStamp();

            long DaysInMillis = TimeUnit.DAYS.toMillis(1);
            if (timeDifference > DaysInMillis) {
                if (checkOwner(abortDTO.getEmail(), item.getCurrentOwner())) {
                    if (item.getStatus() == 0)
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has been cancelled!");
                    // Sử dụng mapper để ánh xạ AbortDTO sang ItemLog
                    // ItemLog itemLog = new ItemLog();
                    Location savedLocation = locationRepository.save(locationMapper.locationDtoToLocation(abortDTO.getLocation()));
                    //  locationRepository.save(locationSaved);
                    Party partySaved = new Party();
                    partySaved.setPartyFullName(item.getCurrentOwner());
                    partySaved.setEmail(item.getCurrentOwner());
                    partyRepository.save(partySaved);
                    double pointX = pointService.generateX();
                    List<ItemLog> pointLogs = itemLogRepository.getPointItemId(item.getItemId());
                    List<Point> pointList = pointService.getPointList(pointLogs);
                    double pointY = pointService.lagrangeInterpolate(pointList, pointX);
                    Point point = new Point(pointX, pointY);
                    itemLogRepository.save(new ItemLog(item, abortDTO.getLocation().getAddress(), partySaved, savedLocation,
                            System.currentTimeMillis(), abortDTO.getDescription(), null, eventTypeRepository.findOneByEventId(5),
                            1, abortDTO.getImageItemLog(), point.toString()));

                    // Cap nhat trang thai cua item => da bi huy
                    itemRepository.updateItemStatus(abortDTO.getProductRecognition(), 0);
                    return ResponseEntity.status(HttpStatus.OK).body("Abort successfully!");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not currentOwner!");
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot destroy this item once created!");
        } catch (Exception ex) {
            logService.logError(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
    }

    //Da test
    public ResponseEntity<?> getItemByEventType(int eventType) {
        List<Item> item = itemRepository.getItemByEventType(eventType);
        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Item found for eventType: " + eventType);
        } else {
            return ResponseEntity.ok(item);
        }
    }


    public JSONObject infoItemForMonitor(long startDate, long endDate) {
        List<Item> monthlyItem = itemRepository.findAllItemByCreatedAtBetween(startDate, endDate);
        List<Item> items = itemRepository.findAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", items.size());
        jsonObject.put("monthly",monthlyItem.size());
        return jsonObject;
    }
    public JSONObject logMetrics() {
            int countPoint = itemLogRepository.countPoint();
            int countItemLog = itemLogRepository.countItemId();

           JSONObject jsonObject = new JSONObject();
            jsonObject.put("countPoint", countPoint);
            jsonObject.put("countItemLog", countItemLog);
            return jsonObject;
    }

    public ResponseEntity<?> getInforItemByItemId(String productRecognition) {
        try {
            ItemDTO itemDTO = new ItemDTO();
            Item item = itemRepository.findByProductRecognition(productRecognition);
            itemDTO.setItemId(item.getItemId());
            itemDTO.setManufacturerName(item.getProduct().getManufacturer().getLastName() + " " + item.getProduct().getManufacturer().getLastName());
            itemDTO.setManufacturerId(item.getProduct().getManufacturer().getUserId());
            itemDTO.setProductId(item.getProduct().getProductId());
            itemDTO.setProductName(item.getProduct().getProductName());
            return ResponseEntity.status(HttpStatus.OK).body(itemDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
