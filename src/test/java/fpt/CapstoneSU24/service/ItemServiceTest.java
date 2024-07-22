//package fpt.CapstoneSU24.service;
//
//import fpt.CapstoneSU24.dto.ItemViewDTOResponse;
//import fpt.CapstoneSU24.dto.payload.FilterSearchItemRequest;
//import fpt.CapstoneSU24.exception.LogService;
//import fpt.CapstoneSU24.mapper.AbortMapper;
//import fpt.CapstoneSU24.mapper.AuthorizedMapper;
//import fpt.CapstoneSU24.mapper.ItemMapper;
//import fpt.CapstoneSU24.mapper.LocationMapper;
//import fpt.CapstoneSU24.model.Item;
//import fpt.CapstoneSU24.repository.*;
//import fpt.CapstoneSU24.util.DocumentGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.http.ResponseEntity;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ItemServiceTest {
//
//    @Mock
//    private LocationRepository locationRepository;
//    @Mock
//    private ItemRepository itemRepository;
//    @Mock
//    private PartyRepository partyRepository;
//    @Mock
//    private ItemLogRepository itemLogRepository;
//    @Mock
//    private QRCodeGenerator qrCodeGenerator;
//    @Mock
//    private ProductRepository productRepository;
//    @Mock
//    private ImageProductRepository imageProductRepository;
//    @Mock
//    private ClientService clientService;
//    @Mock
//    private AuthorizedRepository authorizedRepository;
//    @Mock
//    private EventTypeRepository eventTypeRepository;
//    @Mock
//    private ExportExcelService exportExcelService;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private PointService pointService;
//    @Mock
//    private SpringTemplateEngine templateEngine;
//    @Mock
//    private DocumentGenerator documentGenerator;
//    @Mock
//    private OriginRepository originRepository;
//    @Mock
//    private CloudinaryService cloudinaryService;
//    @Mock
//    private LogService logService;
//    @Mock
//    private ItemMapper itemMapper;
//    @Mock
//    private AbortMapper abortMapper;
//    @Mock
//    private LocationMapper locationMapper;
//    @Mock
//    private AuthorizedMapper authorizedMapper;
//
//    @InjectMocks
//    private ItemService itemService; // Lớp mà bạn đang kiểm thử
//
//    private FilterSearchItemRequest request;
//    @BeforeEach
//    public void setUp() {
//        request = new FilterSearchItemRequest();
//        request.setPageNumber(0);
//        request.setPageSize(10);
//        request.setType("desc");
//        request.setStartDate(0);
//        request.setEndDate(0);
//        request.setProductId(1);  // Đây là kiểu Long
//        request.setName("example");
//        request.setProductRecognition("hfirnxvbtt");
//    }
//
//    @Test
//    public void testSearchItemWithValidData() {
//        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Item item = new Item();
//        Page<Item> page = new PageImpl<>(Collections.singletonList(item));
//
//        when(itemRepository.findAllItem(anyInt(), anyString(), anyString(), eq(pageable))).thenReturn(page);
//        when(itemMapper.itemToItemViewDTOResponse(any(Item.class))).thenReturn(new ItemViewDTOResponse(anyInt(), anyString(),anyLong(),anyInt()));
//
//        ResponseEntity<?> responseEntity = itemService.searchItem(request);
//        assertEquals(200, responseEntity.getStatusCodeValue());
//    }
//
//    @Test
//    public void testSearchItemWithException() {
//        when(itemRepository.findAllItem(anyInt(), anyString(), anyString(), any(Pageable.class))).thenThrow(new RuntimeException("Database error"));
//
//        ResponseEntity<?> responseEntity = itemService.searchItem(request);
//        assertEquals(500, responseEntity.getStatusCodeValue());
//        assertEquals("Error when fetching data", responseEntity.getBody());
//    }
//
//    @Test
//    void exportListItem() {
//    }
//
//    @Test
//    void addItem() {
//    }
//
//    @Test
//    void viewLineItem() {
//    }
//
//    @Test
//    void viewOrigin() {
//    }
//
//    @Test
//    void getCertificate() {
//    }
//
//    @Test
//    void confirmCurrentOwner() {
//    }
//
//    @Test
//    void checkEventAuthorized() {
//    }
//
//    @Test
//    void authorize() {
//    }
//
//    @Test
//    void check() {
//    }
//
//    @Test
//    void checkOwner() {
//    }
//
//    @Test
//    void findByProductRecognition() {
//    }
//
//    @Test
//    void addEventAuthorized() {
//    }
//
//    @Test
//    void sendCurrentOwnerOTP() {
//    }
//
//    @Test
//    void sendOTP() {
//    }
//
//    @Test
//    void confirmOTP() {
//    }
//
//    @Test
//    void abortItem() {
//    }
//
//    @Test
//    void getItemByEventType() {
//    }
//}