package fpt.CapstoneSU24.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.CapstoneSU24.dto.AbortDTO;
import fpt.CapstoneSU24.dto.LocationDTO;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemLogRepository itemLogRepository;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private PartyRepository partyRepository;
//x
    @MockBean
    private PointService pointService;

    @MockBean
    private EventTypeRepository eventTypeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private LogService logService;

    @BeforeEach
    public void setUp() {
        Item item = new Item();
        item.setItemId(55);
        when(itemRepository.findByProductRecognition("someRecognition")).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(55)).thenReturn(Collections.emptyList());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/datatestAbortItem.csv", numLinesToSkip = 1)
    public void testAbortItem2(int itemId, String productRecognition, String email, String locationAddress,
                              String locationCity, String locationCountry, String locationDistrict,
                              String locationWard, double locationCoordinateX, double locationCoordinateY,
                              String description, int expectedStatusCode, String expectedBody) throws Exception {

        // Arrange
        AbortDTO abortDTO = new AbortDTO();
        abortDTO.setProductRecognition(productRecognition);
        abortDTO.setEmail(email);

        LocationDTO location = new LocationDTO();
        location.setAddress(locationAddress);
        location.setCity(locationCity);
        location.setCountry(locationCountry);
        location.setDistrict(locationDistrict);
        location.setWard(locationWard);
        location.setCoordinateX(locationCoordinateX);
        location.setCoordinateY(locationCoordinateY);

        abortDTO.setLocation(location);
        abortDTO.setDescription(description);

        Item item = new Item();
        item.setItemId(itemId);

        when(itemRepository.findByProductRecognition(productRecognition)).thenReturn(item);
        when(itemLogRepository.getItemLogsByItemId(itemId)).thenReturn(Collections.emptyList());
        // Act & Assert
//        mockMvc.perform(post("/api/item/abortItem") // giả sử endpoint của bạn là /api/items/abort
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(abortDTO)))
//                .andExpect(status().is(expectedStatusCode))
//                .andExpect((ResultMatcher) content().string(expectedBody));

        verify(itemRepository, times(1)).findByProductRecognition(productRecognition);
        verify(itemLogRepository, times(1)).getItemLogsByItemId(itemId);
    }
}
