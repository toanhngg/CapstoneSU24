package fpt.CapstoneSU24.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.CapstoneSU24.dto.AuthorizedDTO;
import fpt.CapstoneSU24.dto.CustomerCareDTO;
import fpt.CapstoneSU24.dto.CustomerCareRponseDTO;
import fpt.CapstoneSU24.model.User;

import fpt.CapstoneSU24.model.Authorized;
import fpt.CapstoneSU24.model.CustomerCare;
import jakarta.persistence.Column;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CustomerCareMapper {
    @Mapping(source = "careId", target = "careId")
    @Mapping(source = "customerName", target = "customerName")
    @Mapping(source = "customerEmail", target = "customerEmail")
    @Mapping(source = "customerPhone", target = "customerPhone")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "timestamp", target = "timestamp")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "note", target = "note")
    @Mapping(target = "username", expression = "java(getFullName(customerCare.getUserCheck()))")
    CustomerCareRponseDTO customerCareToCustomerCareDTO(CustomerCare customerCare);

    default String getFullName(User user) {
        if (user == null) {
            return null;
        }
        if(user.getLastName() == null){
            return user.getFirstName();
        } else if (user.getFirstName() == null) {
            return user.getLastName();

        } else
        return user.getFirstName() + " " + user.getLastName();
    }
}