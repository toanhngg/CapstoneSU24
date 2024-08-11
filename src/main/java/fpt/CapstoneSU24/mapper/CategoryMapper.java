package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.CategoryByUserDTO;
import fpt.CapstoneSU24.dto.ItemViewDTOResponse;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CategoryMapper {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemLogRepository itemLogRepository;

    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "name", target = "name")

    public abstract List<CategoryByUserDTO> categoryToCategoryByUserDTO(List<Category> categoryList);




//    @AfterMapping
//    protected void setAfter(Item item, @MappingTarget ItemViewDTOResponse itemViewDTOResponse) {
//
//    }
}