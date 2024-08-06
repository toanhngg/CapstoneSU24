package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CurrentOwnerCheckDTO;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.Authorized;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import org.cloudinary.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_SendOTP {

    @Mock
    private ItemRepository itemRepository;
    //x
    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private LogService logService;

    @InjectMocks
    private ItemService itemService; // replace YourService with the actual service class name

    private CurrentOwnerCheckDTO req;
    private Item item;
    private ItemLog itemLog;
    private List<ItemLog> itemLogList;

    @BeforeEach
    public void setUp() {
        req = new CurrentOwnerCheckDTO();
        req.setEmail("test@example.com");
        req.setProductRecognition("product123");

        item = new Item();
        item.setItemId(1);
        item.setStatus(1);
        item.setProductRecognition("product123");

        Authorized authorized = new Authorized();
        authorized.setAuthorizedEmail("test@example.com");
        authorized.setAuthorizedName("Test User");

        Location location = new Location();
        location.setAddress("123 Test St");

       // authorized.setLocation(location);

        itemLog = new ItemLog();
        itemLog.setAuthorized(authorized);

        itemLogList = Arrays.asList(itemLog);
    }

    @Test
    public void testSendOTP_Success() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogList);
        when(clientService.createMailAndSaveSQL(any(ClientSdi.class))).thenReturn(true);

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OTP has been sent successfully.", response.getBody());
    }

    @Test
    public void testSendOTP_ItemNotFound() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(null);

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Product not found!", response.getBody());
    }

    @Test
    public void testSendOTP_ItemStatusZero() throws Exception {
        item.setStatus(0);
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(false, response.getBody());
    }

    @Test
    public void testSendOTP_ItemLogNotFound() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(Arrays.asList());

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("ItemLog not found!", response.getBody());
    }

    @Test
    public void testSendOTP_ItemNotAuthorized() throws Exception {
        itemLog.setAuthorized(null);
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogList);

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("This product has not been authorized!", response.getBody());
    }

    @Test
    public void testSendOTP_EmailNotCurrentOwner() throws Exception {
        itemLog.getAuthorized().setAuthorizedEmail("other@example.com");
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogList);

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("You are not the current owner.", response.getBody());
    }

    @Test
    public void testSendOTP_EmailSendFailed() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogList);
        when(clientService.createMailAndSaveSQL(any(ClientSdi.class))).thenReturn(false);

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to send OTP.", response.getBody());
    }

    @Test
    public void testSendOTP_UnexpectedError() throws Exception {
        doThrow(new RuntimeException("Unexpected Error")).when(itemRepository).findByProductRecognition(anyString());

        ResponseEntity<?> response = itemService.sendOTP(req);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An unexpected error occurred.", response.getBody());
    }
}