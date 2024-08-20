package fpt.CapstoneSU24.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpt.CapstoneSU24.dto.AbortDTO;
import fpt.CapstoneSU24.dto.LocationDTO;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.LocationMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_AbortItem {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private PointService pointService;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private LogService logService;

    private AbortDTO abortDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        abortDTO = new AbortDTO();
        abortDTO.setProductRecognition("ExMmZiNzFh");
        abortDTO.setEmail("test@example.com");
        abortDTO.setPartyFullName("John Doe");
        abortDTO.setDescription("Sample description");
        abortDTO.setOTP("123456");
        // Initialize other fields as needed
    }

    @Test
    public void testAbortItem_Success() {
        // Prepare mock data
        Item item = new Item();
        item.setItemId(1);
        item.setCurrentOwner("test@example.com");
        item.setStatus(1); // Status should not be 0 for successful abort

        ItemLog itemLog = new ItemLog();
        itemLog.setTimeStamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) - 1000);

        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));
        when(clientService.checkOTP("test@example.com", "123456", "product123")).thenReturn(3);
        when(locationRepository.save(any())).thenReturn(new Location());
        when(partyRepository.save(any())).thenReturn(new Party());
        when(pointService.generateX()).thenReturn(1.0);
        when(pointService.getPointList(any())).thenReturn(Collections.emptyList());
        when(pointService.lagrangeInterpolate(any(), anyDouble())).thenReturn(1.0);
        when(eventTypeRepository.findOneByEventId(5)).thenReturn(new EventType());

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Abort successfully!", response.getBody());
    }

    @Test
    public void testAbortItem_ItemNotFound() {
        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenReturn(null);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Item not found!", response.getBody());
    }

    @Test
    public void testAbortItem_ListEmpty() {
        Item item = new Item();
        item.setItemId(1);

        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("List not found!", response.getBody());
    }

    @Test
    public void testAbortItem_OTPIncorrect() {
        Item item = new Item();
        item.setItemId(1);
        item.setCurrentOwner("test@example.com");

        ItemLog itemLog = new ItemLog();
        itemLog.setTimeStamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) - 1000);

        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));
        when(clientService.checkOTP("test@example.com", "123456", "ExMmZiNzFh")).thenReturn(6);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Edit fail! OTP is not correct.", response.getBody());
    }

    @Test
    public void testAbortItem_NotOwner() {
        Item item = new Item();
        item.setItemId(1);
        item.setCurrentOwner("other@example.com");

        ItemLog itemLog = new ItemLog();
        itemLog.setTimeStamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) - 1000);

        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));
        when(clientService.checkOTP("test@example.com", "123456", "ExMmZiNzFh")).thenReturn(3);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You are not currentOwner!", response.getBody());
    }

    @Test
    public void testAbortItem_TimeDifferenceTooLong() {
        Item item = new Item();
        item.setItemId(1);
        item.setCurrentOwner("test@example.com");

        ItemLog itemLog = new ItemLog();
        itemLog.setTimeStamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2) - 1000);

        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(1)).thenReturn(Collections.singletonList(itemLog));

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You cannot destroy this item once created!", response.getBody());
    }

    @Test
    public void testAbortItem_Exception() {
        when(itemRepository.findByProductRecognition("ExMmZiNzFh")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Error: Database error"));
    }
}