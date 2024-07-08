package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.ItemViewDTOResponse;
import fpt.CapstoneSU24.dto.LocationDTO;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Location;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface  LocationMapper {
    @Mapping(source = "address", target = "address")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "coordinateX", target = "coordinateX")
    @Mapping(source = "coordinateY", target = "coordinateY")
    @Mapping(source = "district", target = "district")
    @Mapping(source = "ward", target = "ward")
    Location locationDtoToLocation(LocationDTO location);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "coordinateX", target = "coordinateX")
    @Mapping(source = "coordinateY", target = "coordinateY")
    @Mapping(source = "district", target = "district")
    @Mapping(source = "ward", target = "ward")
    LocationDTO locationToLocationDto(Location location);
}
