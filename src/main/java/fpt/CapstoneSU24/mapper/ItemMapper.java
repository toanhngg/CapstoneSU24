package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.ItemLogDetailResponse;
import fpt.CapstoneSU24.dto.ItemViewDTOResponse;
import fpt.CapstoneSU24.dto.ProductDTOResponse;
import fpt.CapstoneSU24.dto.ProductDetailDTOResponse;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.ImageProductRepository;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.service.CloudinaryService;
import fpt.CapstoneSU24.service.ItemLogService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ItemMapper {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemLogRepository itemLogRepository;

    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "productRecognition", target = "productRecognition")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "statusEventType", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "coordinateX", ignore = true)
    @Mapping(target = "coordinateY", ignore = true)
    public abstract ItemViewDTOResponse itemToItemViewDTOResponse(Item item);




    @AfterMapping
    protected void setAfter(Item item, @MappingTarget ItemViewDTOResponse itemViewDTOResponse) {
        Optional<ItemLog> itemLogOptional = itemLogRepository.findFirstByItem_ItemIdOrderByItemLogIdDesc(item.getItemId());
        if (itemLogOptional.isPresent()) {
            ItemLog itemLog = itemLogOptional.get();
            if(itemLog.getLocation() != null){
                itemViewDTOResponse.setCoordinateX(itemLog.getLocation().getCoordinateX());
                itemViewDTOResponse.setCoordinateY(itemLog.getLocation().getCoordinateY());
                itemViewDTOResponse.setAddress(itemLog.getLocation().getAddress());
            }
            itemViewDTOResponse.setStatusEventType(itemLog.getEvent_id().getEvent_type());
        } else {
            // Handle the case where no log entry was found
            itemViewDTOResponse.setCoordinateX(0); // or some default value
            itemViewDTOResponse.setCoordinateY(0); // or some default value
            itemViewDTOResponse.setAddress("");
            itemViewDTOResponse.setStatusEventType("");
        }
    }
}