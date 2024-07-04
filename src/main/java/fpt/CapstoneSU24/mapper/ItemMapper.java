package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.ItemViewDTOResponse;
import fpt.CapstoneSU24.dto.ProductDTOResponse;
import fpt.CapstoneSU24.dto.ProductDetailDTOResponse;
import fpt.CapstoneSU24.model.ImageProduct;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.repository.ImageProductRepository;
import fpt.CapstoneSU24.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ItemMapper {
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "productRecognition", target = "productRecognition")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "status", target = "status")

    public abstract ItemViewDTOResponse itemToItemViewDTOResponse(Item item);
}