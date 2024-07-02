package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.UserVerifyDTOResponse;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {
    @Autowired
    CloudinaryService cloudinaryService;
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "org_name", target = "org_name")
    @Mapping(target = "profileImage", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract UserVerifyDTOResponse usersToUserDTOs(User users);

    @AfterMapping
    protected void setAvatar(User user, @MappingTarget UserVerifyDTOResponse UserVerifyDTOResponse) {
        UserVerifyDTOResponse.setProfileImage(cloudinaryService.getImageUrl(user.getProfileImage()));
    }
}
