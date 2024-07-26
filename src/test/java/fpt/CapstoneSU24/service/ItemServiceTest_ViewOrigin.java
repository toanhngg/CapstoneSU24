package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.controller.ItemController;
import fpt.CapstoneSU24.dto.OriginDTO;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.mapper.LocationMapper;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.repository.ImageProductRepository;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
  @ExtendWith(MockitoExtension.class)
public class ItemServiceTest_ViewOrigin {

    @Mock
    private ItemLogRepository itemLogRepository;

    @Mock
    private ImageProductRepository imageProductRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private LogService logService;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private ItemService itemService; // replace with your actual controller class name

    private ItemLog itemLog;
    private Item item;
    private Product product;
    private Origin origin;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProductName("Product Name");
        product.setDescription("Product Description");
        product.setWarranty(1);
        product.setProductId(1);

        origin = new Origin();
        origin.setOrg_name("Org Name");
        origin.setPhone("1234567890");
        origin.setFullNameManufacturer("Full Name Manufacturer");
        origin.setDescription("Origin Description");

        item = new Item();
        item.setProduct(product);
        item.setOrigin(origin);

        itemLog = new ItemLog();
        itemLog.setItem(item);
    }

    @Test
    public void testViewOrigin_ItemLogNotFound() {
        int itemLogId = 1;
        when(itemLogRepository.getItemLogs(itemLogId)).thenReturn(null);

        ResponseEntity<?> response = itemService.viewOrigin(itemLogId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ItemLog not found.", response.getBody());
    }

    @Test
    public void testViewOrigin_ItemLogFound() {
        int itemLogId = 1;
        when(itemLogRepository.getItemLogs(itemLogId)).thenReturn(itemLog);
        when(imageProductRepository.findAllFilePathNotStartingWithAvatar(product.getProductId())).thenReturn(List.of("image1.png", "image2.png"));
        when(cloudinaryService.getImageUrl("image1.png")).thenReturn("http://image1.png");
        when(cloudinaryService.getImageUrl("image2.png")).thenReturn("http://image2.png");

        ResponseEntity<?> response = itemService.viewOrigin(itemLogId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        OriginDTO originDTO = (OriginDTO) response.getBody();
        assertEquals(item.getCreatedAt(), originDTO.getCreateAt());
        assertEquals(product.getProductName(), originDTO.getProductName());
        assertEquals(item.getProductRecognition(), originDTO.getProductRecognition());
        assertEquals(origin.getOrg_name(), originDTO.getOrgName());
        assertEquals(origin.getPhone(), originDTO.getPhone());
        // Add more assertions to verify other fields

        verify(locationMapper).locationToLocationDto(origin.getLocation());
        verify(cloudinaryService).getImageUrl("image1.png");
        verify(cloudinaryService).getImageUrl("image2.png");
    }

    @Test
    public void testViewOrigin_Exception() {
        int itemLogId = 1;
        when(itemLogRepository.getItemLogs(itemLogId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = itemService.viewOrigin(itemLogId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(logService).logError(any(RuntimeException.class));
    }
}
