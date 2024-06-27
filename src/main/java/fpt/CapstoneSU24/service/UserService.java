package fpt.CapstoneSU24.service;


import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.AuthTokenRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final AuthTokenRepository authTokenRepository;

    private final UserRepository userRepository;

    private final CloudinaryService cloudinaryService;

    @Autowired
    public UserService(AuthTokenRepository authTokenRepository,UserRepository userRepository,
                       CloudinaryService cloudinaryService){
        this.authTokenRepository = authTokenRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }

    public UserProfileDTO getUserProfile(Authentication authentication, int userId) {
        UserProfileDTO userProfileDTO = null;
        boolean isAdmin =false;
        try {
            User currentUser;
            User checkUser = null;

            if (authentication.getPrincipal() instanceof User){
                checkUser = (User) authentication.getPrincipal();
                isAdmin = checkUser.getRole().getRoleId() == 1;
            }

            if (userId == -1) {
                currentUser = (User) authentication.getPrincipal();
            } else {
                currentUser = userRepository.findOneByUserId(userId);
            }

            if (currentUser != null) {
/*                AuthToken authToken = authTokenRepository.findOneById(currentUser.getUserId());
                if (authToken != null) {*/
                    userProfileDTO = new UserProfileDTO();
                    userProfileDTO.setEmail(currentUser.getEmail());
                    userProfileDTO.setRole(currentUser.getRole());
                    userProfileDTO.setFirstName(currentUser.getFirstName());
                    userProfileDTO.setLastName(currentUser.getLastName());
                    userProfileDTO.setDescription(currentUser.getDescription());
                    userProfileDTO.setPhone(currentUser.getPhone());
                    userProfileDTO.setStatus(currentUser.getStatus());
                    userProfileDTO.setAddress(currentUser.getLocation().getAddress());
                    userProfileDTO.setCity(currentUser.getLocation().getCity());
                    userProfileDTO.setCountry(currentUser.getLocation().getCountry());
                    //cloudinaryService.getImageUrl: In: Key của ảnh(đã upload len, xem trong db), Out: Đuong dan cua anh
                    userProfileDTO.setProfileIMG(cloudinaryService.getImageUrl(currentUser.getProfileImage()));
                    userProfileDTO.setWard(currentUser.getLocation().getWard());
                    userProfileDTO.setDistrict(currentUser.getLocation().getDistrict());

                if (userId > 0 && !isAdmin) {
                    if (checkUser == null || (checkUser.getUserId() != userId)) {
                        userProfileDTO.setEmail("");
                        userProfileDTO.setPhone("");
                    }
                }


            }
        } catch (Exception e) {
            System.out.println("loi: " + e.getMessage());
        }
        return userProfileDTO;
    }
}