package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CurrentOwnerCheckDTO;
import fpt.CapstoneSU24.dto.SendOTP;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_sendCurrentOwnerOTP {

    @Mock
    private ItemRepository itemRepository;
    //x
    @Mock
    private ClientService clientService;

    @Mock
    private LogService logService;

    @InjectMocks
    private ItemService yourService; // replace YourService with the actual service class name

    private CurrentOwnerCheckDTO req;
    private Item item;

    @BeforeEach
    public void setUp() {
        req = new CurrentOwnerCheckDTO();
        req.setEmail("test@example.com");
        req.setProductRecognition("product123");

        item = new Item();
        item.setItemId(1);
        item.setStatus(1);
        item.setProductRecognition("product123");
        item.setCurrentOwner("test@example.com");
    }

    @Test
    public void testSendCurrentOwnerOTP_Success() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(clientService.createMailAndSaveSQL(any(ClientSdi.class))).thenReturn(true);

        ResponseEntity<?> response = yourService.sendCurrentOwnerOTP(req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OTP has been sent successfully.", response.getBody());
    }

    @Test
    public void testSendCurrentOwnerOTP_ProductNotFound() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(null);

        ResponseEntity<?> response = yourService.sendCurrentOwnerOTP(req);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Product not found!", response.getBody());
    }

    @Test
    public void testSendCurrentOwnerOTP_ProductCancelled() throws Exception {
        item.setStatus(0);
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);

        ResponseEntity<?> response = yourService.sendCurrentOwnerOTP(req);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("This product has been cancelled!", response.getBody());
    }

    @Test
    public void testSendCurrentOwnerOTP_NotCurrentOwner() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);

        ResponseEntity<?> response = yourService.sendCurrentOwnerOTP(req);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to send OTP.", response.getBody());
    }

    @Test
    public void testSendCurrentOwnerOTP_EmailSendFailed() throws Exception {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);

        ResponseEntity<?> response = yourService.sendCurrentOwnerOTP(req);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to send OTP.", response.getBody());
    }

    @Test
    public void testSendCurrentOwnerOTP_UnexpectedError() throws Exception {
        doThrow(new RuntimeException("Unexpected Error")).when(itemRepository).findByProductRecognition(anyString());

        ResponseEntity<?> response = yourService.sendCurrentOwnerOTP(req);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An unexpected error occurred.", response.getBody());
    }
}
