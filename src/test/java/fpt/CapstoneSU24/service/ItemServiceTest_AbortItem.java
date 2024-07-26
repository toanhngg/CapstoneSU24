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
import fpt.CapstoneSU24.model.EventType;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_AbortItem {
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
    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;
    @InjectMocks
    private ItemService itemService;

    private AbortDTO abortDTO;
    private Item item;
    private List<ItemLog> itemLogs;

    @BeforeEach
    public void setup() {
        abortDTO = new AbortDTO();
        abortDTO.setProductRecognition("product123");
        abortDTO.setEmail("owner@example.com");
        // Setup other fields of abortDTO as necessary
        // Create and set a valid LocationDTO
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setAddress("123 Test St");
        abortDTO.setLocation(locationDTO);
        item = new Item();
        item.setItemId(1);
        item.setCurrentOwner("owner@example.com");
        item.setStatus(1);

        ItemLog itemLog = new ItemLog();
        itemLog.setTimeStamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(25));
        itemLogs = Collections.singletonList(itemLog);
    }

    @Test
    public void testAbortItemSuccess() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogs);
        when(pointService.generateX()).thenReturn(1.0);
        when(pointService.getPointList(anyList())).thenReturn(Collections.emptyList());
        when(pointService.lagrangeInterpolate(anyList(), anyDouble())).thenReturn(1.0);
        when(eventTypeRepository.findOneByEventId(anyInt())).thenReturn(new EventType());

        Location savedLocation = new Location();
        when(locationMapper.locationDtoToLocation(any())).thenReturn(savedLocation);
        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Abort successfully!", response.getBody());
    }

    @Test
    public void testAbortItemNotOwner() {
        item.setCurrentOwner("another@example.com");

        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogs);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You are not currentOwner!", response.getBody());
    }

    @Test
    public void testAbortItemTooSoon() {
        itemLogs.get(0).setTimeStamp(System.currentTimeMillis());

        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogs);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You cannot destroy this item once created!", response.getBody());
    }

    @Test
    public void testAbortItemAlreadyCancelled() {
        item.setStatus(0);

        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(itemLogs);

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This product has been cancelled!", response.getBody());
    }

    @Test
    public void testAbortItemLogsNotFound() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("List not found!", response.getBody());
    }

    @Test
    public void testAbortItemException() {
        when(itemRepository.findByProductRecognition(anyString())).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<String> response = itemService.abortItem(abortDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Error:"));
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/datatestGetItemByEventType.csv", numLinesToSkip = 1)
    public void testGetItemByEventType(int eventType, int expectedStatusCode) {
        // Convert int to HttpStatus
        HttpStatus expectedStatus = HttpStatus.valueOf(expectedStatusCode);

        // Arrange
        if (expectedStatus.equals(HttpStatus.OK)) {
            Item item = new Item(); // Initialize your Item object with required fields
            when(itemRepository.getItemByEventType(eventType)).thenReturn(List.of(item));
        } else {
            when(itemRepository.getItemByEventType(eventType)).thenReturn(Collections.emptyList());
        }

        // Act
        ResponseEntity<?> response = itemService.getItemByEventType(eventType);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        verify(itemRepository, times(1)).getItemByEventType(eventType);
    }
}