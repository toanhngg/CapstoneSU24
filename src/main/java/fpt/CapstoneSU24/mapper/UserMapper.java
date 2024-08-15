package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.SupporterDTOResponse;
import fpt.CapstoneSU24.dto.UserVerifyDTOResponse;
import fpt.CapstoneSU24.dto.UserViewDTO;
import fpt.CapstoneSU24.dto.UserViewDetailDTO;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CertificateRepository;
import fpt.CapstoneSU24.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    CertificateRepository certificateRepository;
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "org_name", target = "org_name")
    @Mapping(target = "profileImage", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract UserVerifyDTOResponse usersToUserDTOs(User users);
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
//    @Mapping(source = "password", target = "password")
    public abstract SupporterDTOResponse usersToSupporterDTOResponse(User users);
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "org_name", target = "org_name")
    @Mapping(target = "orgImage", ignore = true) // Ignore avatar for now, we'll set it manually
    @Mapping(target = "profileImage", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract UserViewDetailDTO usersToUserViewDetailDTO(User users);
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "org_name", target = "org_name")
    @Mapping(target = "profileImage", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract UserViewDTO usersToUserViewDTOs(User users);
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "org_name", target = "org_name")
    @Mapping(target = "profileImage", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract List<UserViewDTO> usersToUserViewDTOs(List<User> users);
    @AfterMapping
    protected void setAfter(User user, @MappingTarget UserVerifyDTOResponse userVerifyDTOResponse) {
        userVerifyDTOResponse.setProfileImage(cloudinaryService.getImageUrl(user.getProfileImage()));
    }
    @AfterMapping
    protected void setAfter(User user, @MappingTarget UserViewDetailDTO usersToUserViewDetailDTO) {
        usersToUserViewDetailDTO.setProfileImage(cloudinaryService.getImageUrl(user.getProfileImage()));
        usersToUserViewDetailDTO.setOrgImage(cloudinaryService.getImageUrl(user.getOrgImage()));

    }
    @AfterMapping
    protected void setAfter(User user, @MappingTarget UserViewDTO userViewDTO) {
        userViewDTO.setProfileImage(cloudinaryService.getImageUrl(user.getOrgImage()));
    }
}