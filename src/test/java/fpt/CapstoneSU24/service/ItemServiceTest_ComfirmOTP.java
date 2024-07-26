package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.SendOTP;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.Authorized;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.repository.EventTypeRepository;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.repository.PartyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_ComfirmOTP {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private PointService pointService;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private ItemService itemService; // Assuming the class containing confirmOTP is named ItemService

    private SendOTP otp;
    private String productRecognition;
    private Item item;
    private ItemLog itemLog;
    private Authorized authorized;

    @BeforeEach
    public void setup() {
        otp = new SendOTP();
        otp.setEmail("test@example.com");
        otp.setOtp("123456");

        productRecognition = "product123";

        item = new Item();
        item.setItemId(1);
        item.setStatus(1);

        authorized = new Authorized();
        authorized.setAuthorizedEmail("test@example.com");
        authorized.setAuthorizedName("Test User");
        authorized.setPhoneNumber("1234567890");
        authorized.setDescription("Description");
        Location location = new Location();
        location.setAddress("123 Street");
        authorized.setLocation(location);

        itemLog = new ItemLog();
        itemLog.setAuthorized(authorized);
    }

    @Test
    public void testConfirmOTP_ItemNotFound() {
        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(null);

        ResponseEntity<Boolean> response = itemService.confirmOTP(otp, productRecognition);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testConfirmOTP_ItemStatusInvalid() {
        item.setStatus(0);
        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(item);

        ResponseEntity<Boolean> response = itemService.confirmOTP(otp, productRecognition);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testConfirmOTP_NoItemLogsFound() {
        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(item.getItemId())).thenReturn(Collections.emptyList());

        ResponseEntity<Boolean> response = itemService.confirmOTP(otp, productRecognition);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testConfirmOTP_AuthorizedEmailMismatch() {
        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(item.getItemId())).thenReturn(Arrays.asList(itemLog));

        otp.setEmail("wrong@example.com");

        ResponseEntity<Boolean> response = itemService.confirmOTP(otp, productRecognition);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testConfirmOTP_OTPCheckFails() {
        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(item.getItemId())).thenReturn(Arrays.asList(itemLog));
        when(clientService.checkOTPinSQL(otp.getEmail().trim(), otp.getOtp().trim())).thenReturn(false);

        ResponseEntity<Boolean> response = itemService.confirmOTP(otp, productRecognition);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testConfirmOTP_Success() {
        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(item.getItemId())).thenReturn(Arrays.asList(itemLog));
        when(clientService.checkOTPinSQL(otp.getEmail().trim(), otp.getOtp().trim())).thenReturn(true);

        ResponseEntity<Boolean> response = itemService.confirmOTP(otp, productRecognition);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
     //   assertEquals("Database error", response.getHeaders().getFirst("Error-Message"));

    }
}
