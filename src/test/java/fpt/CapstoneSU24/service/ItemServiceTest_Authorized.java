package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.AuthorizedDTO;
import fpt.CapstoneSU24.dto.LocationDTO;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.AuthorizedMapper;
import fpt.CapstoneSU24.mapper.LocationMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.AuthorizedRepository;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ItemServiceTest_Authorized {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private AuthorizedRepository authorizedRepository;

    @Mock
    private AuthorizedMapper authorizedMapper;

    @Mock
    private LocationMapper locationMapper;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    @Mock
    private LogService logService;

    @Mock
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEventAuthorized_Success() throws Exception {
        AuthorizedDTO authorizedDTO = new AuthorizedDTO();
        authorizedDTO.setAuthorizedEmail("anhng130@gmail.com");
        authorizedDTO.setLocation(new LocationDTO());

        Item item = new Item();
        item.setItemId(1);

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn("<span style='color:green'><b>Valid!</b>");

        Location location = new Location();
        when(locationMapper.locationDtoToLocation(any(LocationDTO.class))).thenReturn(location);
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        ItemLog itemLog = new ItemLog();
        itemLog.setEvent_id(new EventType(1));
        itemLog.setTimeStamp(System.currentTimeMillis() - 1000);
        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(List.of(itemLog));

        Authorized authorized = new Authorized();
        when(authorizedMapper.authorizedDtoToAuthorized(any(AuthorizedDTO.class))).thenReturn(authorized);
        when(authorizedRepository.save(any(Authorized.class))).thenReturn(authorized);

        ResponseEntity<String> response = itemService.addEventAuthorized(authorizedDTO, item);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Authorization successful!", response.getBody());
    }

    @Test
    public void testAddEventAuthorized_InvalidEmail() throws Exception {
        AuthorizedDTO authorizedDTO = new AuthorizedDTO();
        authorizedDTO.setAuthorizedEmail("anhng130@gmail.com");
        Item item = new Item();
        item.setItemId(1);

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn("Invalid email");

        ResponseEntity<String> response = itemService.addEventAuthorized(authorizedDTO, item);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
//
//    @Test
//    public void testAddEventAuthorized_ItemLogsNotFound() {
//        AuthorizedDTO authorizedDTO = new AuthorizedDTO();
//        Item item = new Item();
//        item.setItemId(2);
//
//        when(itemLogRepository.getItemLogsByItemId(anyInt())).thenReturn(List.of());
//
//        ResponseEntity<String> response = itemService.addEventAuthorized(authorizedDTO, item);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }

    @Test
    public void testAddEventAuthorized_Exception() throws Exception {
        AuthorizedDTO authorizedDTO = new AuthorizedDTO();
        Item item = new Item();
        item.setItemId(1);

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenThrow(new RuntimeException("System error"));

        ResponseEntity<String> response = itemService.addEventAuthorized(authorizedDTO, item);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
