package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CurrentOwnerCheckDTO;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.service.ClientService;
import fpt.CapstoneSU24.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_GetCertificate {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private PointService pointService;

    @InjectMocks
    private ItemService itemService;

    private CurrentOwnerCheckDTO req;
    private Item item;
    private List<ItemLog> itemLogs;
    private List<ItemLog> pointLogs;

    @BeforeEach
    public void setup() {
        req = new CurrentOwnerCheckDTO();
        req.setEmail("anhng130@gmail.com");
        req.setProductRecognition("MxNDZkYWU1");
        req.setOTP("123456");

        item = new Item();
        item.setItemId(1);
        item.setCurrentOwner("anhng130@gmail.com");
        item.setCertificateLink("http://example.com/certificate.pdf");

        itemLogs = new ArrayList<>();
        ItemLog log = new ItemLog();
        itemLogs.add(log);

        pointLogs = new ArrayList<>(itemLogs);
    }

    @Test
    public void testGetCertificate_ProductRecognitionMissing() {
        req.setProductRecognition("");

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product recognition is required", response.getBody());
    }

    @Test
    public void testGetCertificate_ItemNotFound() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(null);

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Item not found.", response.getBody());
    }

    @Test
    public void testGetCertificate_EmailMissing() {
        req.setEmail("");

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetCertificate_NotCurrentOwner() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(clientService.checkOTP(anyString(), anyString(), anyString())).thenReturn(3);

        item.setCurrentOwner("someoneelse@gmail.com");

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User is not the current owner.", response.getBody());
    }

    @Test
    public void testGetCertificate_InsufficientItemLogs() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(clientService.checkOTP(anyString(), anyString(), anyString())).thenReturn(3);
        when(itemLogRepository.getItemLogsByItemIdIgnoreEdit(anyInt())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Insufficient item logs.", response.getBody());
    }

    @Test
    public void testGetCertificate_MismatchInLogs() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(clientService.checkOTP(anyString(), anyString(), anyString())).thenReturn(3);
        when(itemLogRepository.getItemLogsByItemIdIgnoreEdit(anyInt())).thenReturn(itemLogs);
        when(itemLogRepository.getPointItemIdIgnoreEdit(anyInt())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Mismatch in item logs and point logs.", response.getBody());
    }

    @Test
    public void testGetCertificate_InvalidPointsOnCurve() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(clientService.checkOTP(anyString(), anyString(), anyString())).thenReturn(3);
        when(itemLogRepository.getItemLogsByItemIdIgnoreEdit(anyInt())).thenReturn(itemLogs);
        when(itemLogRepository.getPointItemIdIgnoreEdit(anyInt())).thenReturn(pointLogs);
        when(pointService.arePointsOnCurve(pointLogs)).thenReturn(false);

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Points do not form a valid graph.", response.getBody());
    }

    @Test
    public void testGetCertificate_Success() {
        when(itemRepository.findByProductRecognition(anyString())).thenReturn(item);
        when(clientService.checkOTP(anyString(), anyString(), anyString())).thenReturn(3);
        when(itemLogRepository.getItemLogsByItemIdIgnoreEdit(anyInt())).thenReturn(itemLogs);
        when(itemLogRepository.getPointItemIdIgnoreEdit(anyInt())).thenReturn(pointLogs);
        when(pointService.arePointsOnCurve(pointLogs)).thenReturn(true);

        ResponseEntity<?> response = itemService.getCertificate(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item.getCertificateLink(), response.getBody());
    }
}
