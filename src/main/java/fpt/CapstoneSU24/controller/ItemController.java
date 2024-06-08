package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.*;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.payload.FilterByTimeStampRequest;
import fpt.CapstoneSU24.payload.FilterSearchRequest;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.service.ClientService;
import fpt.CapstoneSU24.service.ExportExcelService;
import fpt.CapstoneSU24.service.QRCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/item")
@RestController
public class ItemController {
    @Autowired
    public LocationRepository locationRepository;
    @Autowired
    public OriginRepository originRepository;
    @Autowired
    public ItemRepository itemRepository;
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
    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthorizedRepository authorizedResponsitory;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    ExportExcelService exportExcelService;
    @Autowired
    UserRepository userRepository;
    /*
     * type is sort type: "desc" or "asc"
     * default data startDate and endDate equal 0 (need insert 2 data)
     * */
    @PostMapping("/search")
    public ResponseEntity searchItem(@Valid @RequestBody FilterSearchRequest req) {
        try {
            Page<Item> items = null;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
//        Page<Item> items = jsonReq.getString("type") == null? itemRepository.findAll(pageable) : jsonReq.getString("type").equals("desc") ? itemRepository.sortItemsByCreatedAtDesc(pageable) :  itemRepository.sortItemsByCreatedAtAsc(pageable);
            if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                items = itemRepository.findByCreatedAtBetween(req.getStartDate(), req.getEndDate(), pageable);

            } else {
                items = itemRepository.findAllByCurrentOwnerContaining(req.getName(), pageable);
            }
            return ResponseEntity.status(200).body(items);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error when fetching data");
        }

    }

    @PostMapping("/exportListItem")
    public ResponseEntity<byte[]>  exportListItem(@Valid @RequestBody FilterByTimeStampRequest req) throws IOException {
        JSONObject jsonReq = new JSONObject(req);
        List<Item> items = itemRepository.findByCreatedAtBetween(req.getStartDate(), req.getEndDate());
        byte[] excelBytes = exportExcelService.exportItemToExcel(items);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "exported_data.xlsx");
        return new ResponseEntity<>(excelBytes, headers, 200);
    }
    @PostMapping("/addItem")
    public ResponseEntity addItem(@RequestBody ItemLogDTO itemLogDTO) {
        try {
            // Lấy thông tin của User
            User user = userRepository.getReferenceById(itemLogDTO.getUserId());

            // Lưu địa chỉ
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
            // generateProductDescription
            origin.setDescription(itemLogDTO.getDescriptionOrigin());
            origin.setSupportingDocuments(qrCodeGenerator.generateProductDescription(itemLogDTO.getProductId(), itemLogDTO.getAddress(), scoreTime));
            origin.setEmail(user.getEmail());
            origin.setFullNameManufacturer(user.getFirstName() + " " + user.getLastName());
            origin.setOrg_name(user.getOrg_name());
            origin.setPhone(user.getPhone());
            origin.setLocation(savedLocation);
            Origin saveOrigin = originRepository.save(origin);

            List<Item> items = new ArrayList<>(itemLogDTO.getQuantity());
            List<Party> parties = new ArrayList<>(itemLogDTO.getQuantity());
            List<ItemLog> itemLogs = new ArrayList<>(itemLogDTO.getQuantity());

            for (int i = 0; i < itemLogDTO.getQuantity(); i++) {
                // Create Item
                Item item = new Item();
                item.setCreatedAt(scoreTime);
                item.setCurrentOwner(user.getEmail());
                item.setProductRecognition(qrCodeGenerator.generateProductCode(itemLogDTO.getProductId()));
                item.setStatus(1);
                item.setOrigin(saveOrigin);
                item.setProduct(productRepository.findOneByProductId(itemLogDTO.getProductId()));
                items.add(item);

                // Create Party
                Party party = new Party();
                party.setDescription(itemLogDTO.getDescriptionOrigin());
                party.setEmail(user.getEmail());
                party.setPartyFullName(user.getFirstName() + " " + user.getLastName());
                party.setPhoneNumber(user.getPhone());
                parties.add(party);

                // Create ItemLog
                ItemLog itemLog = new ItemLog();
                itemLog.setAddress(itemLogDTO.getAddress());
                itemLog.setDescription(itemLogDTO.getDescriptionOrigin());
                itemLog.setEvent_id(eventTypeRepository.findOneByEventId(1)); // tạo mới
                itemLog.setStatus(1);
                itemLog.setTimeStamp(scoreTime);
                itemLog.setItem(item);
                itemLog.setLocation(savedLocation);
                itemLog.setParty(party);
                itemLogs.add(itemLog);
            }

            // Save all entities in batch
            itemRepository.saveAll(items);
            partyRepository.saveAll(parties);
            itemLogRepository.saveAll(itemLogs);

            return ResponseEntity.ok("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }


//    @PostMapping("/addItemByQuantity")
//    public ResponseEntity addItemByQuantity(@RequestBody ItemLogDTO itemLogDTO, @RequestParam int quantity) {
//        try {
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            User currentUser = (User) authentication.getPrincipal();
////            currentUser.
//            Location location = new Location();
//            location.setAddress(itemLogDTO.getAddress());
//            location.setCity(itemLogDTO.getCity());
//            location.setCountry(itemLogDTO.getCountry());
//            location.setCoordinateX(itemLogDTO.getCoordinateX());
//            location.setCoordinateY(itemLogDTO.getCoordinateY());
//            Location savedLocation = locationRepository.save(location);
//
//            long scoreTime = System.currentTimeMillis();
//            Origin origin = new Origin();
//            origin.setCreatedAt(scoreTime);
//            origin.setDescription(itemLogDTO.getDescriptionOrigin());
//            origin.setEmail(itemLogDTO.getEmail()); // mai lay tu User
//            origin.setFullNameManufacturer(itemLogDTO.getFullName()); // mai lay tu User
//            origin.setOrg_name(itemLogDTO.getOrgName());  // mai lay tu User
//            origin.setPhone(itemLogDTO.getPhone()); // mai lay tu User
//            // origin.setSupportingDocuments(itemLogDTO.getSupportingDocuments()); // mai lay tu User
//            origin.setLocation(savedLocation);
//            Origin saveOrigin = originRepository.save(origin);
//            for (int i = 0; i < quantity; i++) {
//                Item item = new Item();
//                item.setCreatedAt(scoreTime);
//                item.setCurrentOwner(itemLogDTO.getEmail());
//                String productRe = qrCodeGenerator.generateProductCode(itemLogDTO.getProductId(),quantity);
//                item.setProductRecognition(productRe);
//                item.setStatus(-1);
//                item.setOrigin(saveOrigin);
//                item.setProduct(productRepository.findOneByProductId(itemLogDTO.getProductId()));
//                Item saveItem = itemRepository.save(item);
//
//                Party party = new Party();
//                party.setDescription(itemLogDTO.getDescriptionParty());
//                party.setEmail(itemLogDTO.getEmail()); // mai lay tu User
//                party.setPartyFullName(itemLogDTO.getFullName()); // mai lay tu User
//                party.setPhoneNumber(itemLogDTO.getPhone()); // mai lay tu User
//                party.setSignature(itemLogDTO.getSignature()); // mai lay tu User
//                Party saveParty = partyRepository.save(party);
//
//                ItemLog itemLog = new ItemLog();
//                itemLog.setAddress(itemLogDTO.getAddress());
//                itemLog.setDescription(itemLogDTO.getDescriptionItemLog());
//                itemLog.setEvent_id(itemLogDTO.getEventId());
//                itemLog.setStatus(itemLogDTO.getStatusItemLog());
//                itemLog.setTimeStamp(scoreTime);
//                itemLog.setItem(saveItem);
//                itemLog.setLocation(savedLocation);
//                itemLog.setParty(saveParty);
//                itemLogRepository.save(itemLog);
//            }
//            return ResponseEntity.ok("ok");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

    @GetMapping("/findAllItemByProductId")
    public ResponseEntity findAllItemByProductId(@RequestParam int ProductId) {
        List<Item> itemList = itemRepository.findByProductId(ProductId);
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
            Item item = itemRepository.findByProductRecognition(productRecognition);
          //  System.out.println(item.getItemId());
            List<ItemLog> itemList = itemLogRepository.getItemLogsByItemId(item.getItemId());

            if (itemList.isEmpty()) {
                // Trả về chỉ thông tin của Item nếu không có ItemLog nào
              //  ItemDTO itemDTO = convertToItemDTO(item);
                return ResponseEntity.ok(new ItemLogDTOResponse(/*itemDTO, */Collections.emptyList()));
            } else {
              //  ItemDTO itemDTO = convertToItemDTO(item);
                List<ItemLogDTOResponse> itemLogDTOs = itemList.stream()
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

    private ItemLogDTOResponse convertToItemLogDTO(ItemLog itemLog) {
        ItemLogDTOResponse dto = new ItemLogDTOResponse();
        dto.setItemLogId(itemLog.getItemLogId());
        dto.setAddress(itemLog.getAddress());
        long timestamp = itemLog.getTimeStamp();
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(date);

        dto.setCreatedAt(formattedDate);
        //EventType eventType = eventTypeRepository.getReferenceById(itemLog.getEvent_id());
        dto.setEventType(itemLog.getEvent_id().getEvent_type());
        dto.setPartyName(itemLog.getParty().getPartyFullName());
        dto.setDescription(itemLog.getDescription());

        return dto;



    }

    private ItemDTO convertToItemDTO(Item item) {
    ItemDTO dto = new ItemDTO();
    dto.setProductRecognition(item.getProductRecognition());
    return dto;
}

    @GetMapping("/viewOrigin")
    public ResponseEntity getOrigin(@RequestParam int itemLogId) {

        ItemLog itemLog = itemLogRepository.getItemLogs(itemLogId);

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
        Integer productId = itemLog.getItem().getProduct().getProductId();
        if (productId == null) {
            originDTO.setImage(null);
        } else {
            ImageProduct imageProduct = imageProductRepository.findByproductId(itemLog.getItem().getProduct().getProductId());
            if (imageProduct == null) {
                originDTO.setImage(null);

            } else {
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


    // API uy quyen
    @PostMapping(value = "/authorized")
    public ResponseEntity authorized(@RequestBody Authorized authorized, @RequestParam int itemLogId) throws MessagingException {
// Cai nay la uy quyen nguoi nhan
        // B1. Kiem tra xem san pham nay da duoc uy quyen chua bang cach check currentOwner voi status la 0
        // - Neu ma co currentOwner roi va co status la 1 thi co nghia la chua uy quyen
        // - Neu ma co currentOwner roi va co status la 0 thi co nghia la da uy quyen roi => khong cho uy quyen nua
        // B2. Neu chua uy quyen thi
        // - Update currentOwner voi email cua nguoi duoc uy quyen va status la 0
        // - Update bang itemLog voi id dai nhat voi authorized_id va bang authorized nhung thong tin cua nguoi duoc uy quyen
        // - Gui mail thong bao cho nguoi dung la bạn da duoc uy quyen
        ItemLog itemLog = itemLogRepository.getItemLogs(itemLogId);
        Item item = itemRepository.findByProductRecognition(itemLog.getItem().getProductRecognition()); //B1
        if (item.getStatus() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This product has been authorized !");
        } else if (item.getStatus() == 1) {
            if (authorized.getAuthorized_email() != null) {
                // Add nguoi ngay vao bang nguoi duoc uy quyen authorized
                Location location = new Location();
                location.setAddress(authorized.getLocation().getAddress());
                location.setCity(authorized.getLocation().getCity());
                location.setCountry(authorized.getLocation().getCountry());
                location.setCoordinateX(authorized.getLocation().getCoordinateX());
                location.setCoordinateY(authorized.getLocation().getCoordinateY());
                Location savedLocation = locationRepository.save(location);
                locationRepository.save(new Location());
               Authorized authorizedSaved =  authorizedResponsitory.save(new Authorized(authorized.getAuthorized_name(),
                        authorized.getAuthorized_email(),itemLog.getParty().getPartyFullName(),item.getCurrentOwner(),
                        savedLocation,authorized.getPhoneNumber(),authorized.getDescription()
                    ));
                // Uy quyen thi update vao bang item voi currentOwner voi status la 0
                itemRepository.updateStatusAndCurrent(item.getItemId(), authorized.getAuthorized_email());
                // add authorized_id vao bang itemlog
                itemLogRepository.updateAuthorized(authorizedSaved.getAuthorized_id(),itemLogId);
                ClientSdi sdi = new ClientSdi();
                sdi.setEmail(authorized.getAuthorized_email());
                sdi.setUsername(authorized.getAuthorized_name());
                sdi.setName(authorized.getAuthorized_name());
                //Gui mail thong bao da duoc uy quyen
                return ResponseEntity.ok(clientService.notification(sdi));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter authorized person's email!");
        }
        return null;
    }

    //API send OTP
    @PostMapping(value = "/sendOTP")
    public ResponseEntity<?> sendOTP(@RequestBody String req, @RequestParam String ProductRecognition) {
        JSONObject jsonReq = new JSONObject(req);
        String email = jsonReq.getString("email");

        // Check xem item có tồn tại và có status = 0 hay không
        Item item = itemRepository.findByProductRecognition(ProductRecognition);

        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }

        if (item.getStatus() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This product has not been authorized!");
        }

        // Nếu status = 0, tiếp tục gửi OTP
        ClientSdi sdi = new ClientSdi();
        sdi.setEmail(email);

        boolean emailSent = clientService.createMailAndSaveSQL(sdi);

        if (emailSent) {
            return ResponseEntity.ok("OTP has been sent successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
        }
    }

    // API verify OTP
    @PostMapping(value = "/confirmOTP")
    public ResponseEntity<Boolean> confirmOTP(@RequestBody SendOTP otp, @RequestParam String ProductRecognition) {
        // B1: Người dùng nhập OTP confirm chính xác bằng cách check OTP trong DB và người dùng nhập
        // - Chính xác => Cập nhật status trong item CurrentOwner thành status là 1
        // - Insert bảng party => Sau khi nhận mới trở thành party

        Item item = itemRepository.findByProductRecognition(ProductRecognition); // B1

        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Nếu item không tồn tại
        }

        if (item.getStatus() == 0) {
            boolean check = clientService.checkOTPinSQL(otp.getEmail(), otp.getOtp());

            if (check) {
                itemRepository.updateStatus(item.getItemId());
                List<ItemLog> list = itemLogRepository.getItemLogsByItemId(item.getItemId());

                if (list.isEmpty() || list.size() <= 1) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Nếu không có log hoặc log không đủ
                }

                ItemLog itemIndex = list.get(1);
                Optional<Authorized> authorizedOpt = authorizedResponsitory.findById(itemIndex.getAuthorized().getAuthorized_id());

                if (!authorizedOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Nếu authorized không tồn tại
                }

                Authorized authorized = authorizedOpt.get();
                Party party = partyRepository.save(new Party(
                        authorized.getDescription(),
                        authorized.getAuthorized_email(),
                        authorized.getAuthorized_name(),
                        authorized.getPhoneNumber()
                ));

                ItemLog itemLog = new ItemLog();
                itemLog.setAddress(authorized.getLocation().getAddress());
                itemLog.setDescription(authorized.getDescription());
                itemLog.setAuthorized(null);
                itemLog.setStatus(1);
                itemLog.setTimeStamp(System.currentTimeMillis());
                itemLog.setItem(item);
                itemLog.setLocation(authorized.getLocation());
                itemLog.setParty(party);
                itemLog.setEvent_id(eventTypeRepository.findOneByEventId(2));
                itemLog.setImageItemLog(null);

                itemLogRepository.save(itemLog);

                return ResponseEntity.ok(true); // Thành công, trả về true
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // OTP không chính xác
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Status không phù hợp
        }
    }

}
