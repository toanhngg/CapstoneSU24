package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.AuthorizedDTO;
import fpt.CapstoneSU24.dto.LocationDTO;
import fpt.CapstoneSU24.model.Authorized;
import fpt.CapstoneSU24.model.Location;
import jakarta.persistence.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthorizedMapper {
    @Mapping(source = "authorizedId", target = "authorizedId")
    @Mapping(source = "authorizedName", target = "authorizedName")
    @Mapping(source = "authorizedEmail", target = "authorizedEmail")
    @Mapping(source = "assignPerson", target = "assignPerson")
    @Mapping(source = "assignPersonMail", target = "assignPersonMail")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    Authorized authorizedDtoToAuthorized(AuthorizedDTO authorizedDto);
}
