package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.SendOTP;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.EventTypeRepository;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.repository.PartyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.elasticsearch.common.lucene.uid.Versions.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_ComfirmOTP {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private LogService logService;

    private SendOTP sendOTP;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        sendOTP = new SendOTP();
        sendOTP.setEmail("test@example.com");
        sendOTP.setOtp("123456");
        sendOTP.setDescription("Sample description");
    }

    @Test
    public void testConfirmOTP_Success() {
        Item item = new Item();
        item.setItemId(1);
        item.setStatus(1); // Item status should not be 0 for success

        ItemLog itemLog = new ItemLog();
        itemLog.setAuthorized(new Authorized("John Doe", "test@example.com", "1234567890", "authorized@example.com"));

        when(itemRepository.findByProductRecognition("product123")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));
        when(clientService.checkOTP("test@example.com", "123456", "product123")).thenReturn(2);
        doNothing().when(itemRepository).updateItemStatusAndCurrentOwnwe(1, 1, "test@example.com");
        when(partyRepository.save(any())).thenReturn(new Party());
        when(eventTypeRepository.findOneByEventId(4)).thenReturn(new EventType());

        ResponseEntity<?> response = itemService.confirmOTP(sendOTP, "product123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody());
    }

    @Test
    public void testConfirmOTP_ItemNotFound() {
        when(itemRepository.findByProductRecognition("product123")).thenReturn(null);

        ResponseEntity<?> response = itemService.confirmOTP(sendOTP, "product123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Item not found", response.getBody());
    }

    @Test
    public void testConfirmOTP_ItemAborted() {
        Item item = new Item();
        item.setItemId(1);
        item.setStatus(0); // Item status is 0

        when(itemRepository.findByProductRecognition("product123")).thenReturn(item);

        ResponseEntity<?> response = itemService.confirmOTP(sendOTP, "product123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Item was aborted", response.getBody());
    }

    @Test
    public void testConfirmOTP_IncorrectOTP() {
        Item item = new Item();
        item.setItemId(1);
        item.setStatus(1);

        ItemLog itemLog = new ItemLog();
        itemLog.setAuthorized(new Authorized("John Doe", "test@example.com", "1234567890", "authorized@example.com"));

        when(itemRepository.findByProductRecognition("product123")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));
        when(clientService.checkOTP("test@example.com", "123456", "product123")).thenReturn(6);

        ResponseEntity<?> response = itemService.confirmOTP(sendOTP, "product123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testConfirmOTP_NotAuthorized() {
        Item item = new Item();
        item.setItemId(1);
        item.setStatus(1);

        ItemLog itemLog = new ItemLog();
        itemLog.setAuthorized(new Authorized("John Doe", "test@example.com", "1234567890", "authorized@example.com"));

        when(itemRepository.findByProductRecognition("product123")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));
        when(clientService.checkOTP("test@example.com", "123456", "product123")).thenReturn(3);

        ResponseEntity<?> response = itemService.confirmOTP(sendOTP, "product123");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not authorized product", response.getBody());
    }

    @Test
    public void testConfirmOTP_Exception() {
        when(itemRepository.findByProductRecognition("product123")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = itemService.confirmOTP(sendOTP, "product123");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey("Error-Message"));
        assertEquals("Database error", response.getHeaders().getFirst("Error-Message"));
    }
}
