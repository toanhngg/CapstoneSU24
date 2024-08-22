package fpt.CapstoneSU24.service;


import fpt.CapstoneSU24.dto.B03.B03_GetDataGridDTO;
import fpt.CapstoneSU24.dto.B03.B03_RequestDTO;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.OrgNameUserDTO;
import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.dto.payload.FilterSearchManufacturerRequest;
import fpt.CapstoneSU24.dto.payload.FilterSearchSupporterRequest;
import fpt.CapstoneSU24.dto.payload.IdRequest;
import fpt.CapstoneSU24.dto.payload.OrgNameRequest;
import fpt.CapstoneSU24.mapper.UserMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.repository.AuthTokenRepository;
import fpt.CapstoneSU24.repository.CertificateRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.Const;
import fpt.CapstoneSU24.util.DocumentGenerator;
import fpt.CapstoneSU24.util.TimeStampUtil;
import jakarta.mail.MessagingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private EmailService mailService;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private DocumentGenerator documentGenerator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    TimeStampUtil timeStampUtil;
    @Autowired
    ItemRepository itemRepository;


    public UserProfileDTO getUserProfile(Authentication authentication, int userId) {
        UserProfileDTO userProfileDTO = null;
        boolean isAdmin = false;
        try {
            User currentUser;
            User checkUser = null;

            if (authentication.getPrincipal() instanceof User) {
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
                userProfileDTO.setOrgIMG(cloudinaryService.getImageUrl(currentUser.getOrgImage()));
                userProfileDTO.setWard(currentUser.getLocation().getWard());
                userProfileDTO.setDistrict(currentUser.getLocation().getDistrict());
                userProfileDTO.setOrgName(currentUser.getOrg_name());
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

    public ResponseEntity<String> updateOrgName(String req) {
        JSONObject jsonReq = new JSONObject(req);
        String description = jsonReq.has("orgName") ? jsonReq.getString("orgName") : "";
        User user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication.getPrincipal() instanceof User)) {
            user = (User) authentication.getPrincipal();
            user.setOrg_name(description);
            userRepository.save(user);
        }
        return ResponseEntity.status(200).body("orgName update success");
    }

    public ResponseEntity<?> viewAllManufacturer(FilterSearchManufacturerRequest req) {
        try {
            Page<User> users;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
            users = userRepository.findAllUser("%" + req.getOrgName() + "%", pageable);
            return ResponseEntity.status(200).body(users.map(userMapper::usersToUserViewDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }
    public ResponseEntity<?> getAllUser(FilterSearchManufacturerRequest req) {
        try {
            Page<User> users;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
            users = userRepository.findAllUser("%"+req.getOrgName()+"%", pageable);
            return ResponseEntity.status(200).body(users.map(userMapper::usersToUserViewDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }

    public ResponseEntity<?> getDetailUser(IdRequest req) {
        try {
            User user = userRepository.findOneByUserId(req.getId());
            if (user == null) {
                return ResponseEntity.status(500).body("user id is not exist");
            }
            return ResponseEntity.status(200).body(userMapper.usersToUserViewDetailDTO(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }

    public ResponseEntity<?> getUsersByEmail(B03_RequestDTO userRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);

        if (userProfileDTO.getRole().getRoleId() != 1) {
            return new ResponseEntity<>("Admin role required", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Sort.Direction direction = userRequestDTO.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, userRequestDTO.getOrderBy());

        // Convert Date
        Long timestampFrom = userRequestDTO.getDateFrom() != null ?
                userRequestDTO.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() : null;
        Long timestampTo = userRequestDTO.getDateTo() != null ?
                userRequestDTO.getDateTo().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() : null;

        // Chia Page
        Pageable pageable = PageRequest.of(userRequestDTO.getPage(), userRequestDTO.getSize(), sort);

        // Xử lý giá trị status
        Integer status = null;
        if (userRequestDTO.getStatus() != null && !userRequestDTO.getStatus().isEmpty()) {
            status = Integer.parseInt(userRequestDTO.getStatus());
        }

        Page<User> userPage = userRepository.findByFilters(userRequestDTO.getEmail(), /*userRequestDTO.getRoleId()*/ 2, status,userRequestDTO.getCity() , timestampFrom, timestampTo, pageable);

        if (status != null && status == 0) {
            Page<User> userPageStatus7 = userRepository.findByFilters(userRequestDTO.getEmail(), /*userRequestDTO.getRoleId()*/ 2, 7,userRequestDTO.getCity(), timestampFrom, timestampTo, pageable);

            List<User> combinedUsers = new ArrayList<>(userPage.getContent());
            combinedUsers.addAll(userPageStatus7.getContent());

            userPage = new PageImpl<>(combinedUsers, pageable, userPage.getTotalElements() + userPageStatus7.getTotalElements());
        }

        // mapping DTO
        Page<B03_GetDataGridDTO> B03_GetDataGridDTOPage = userPage.map(user -> {
            B03_GetDataGridDTO B03_GetDataGridDTO = new B03_GetDataGridDTO();
            B03_GetDataGridDTO.setUserId(user.getUserId());
            B03_GetDataGridDTO.setEmail(user.getEmail());
            B03_GetDataGridDTO.setRoleId(user.getRole().getRoleId());
            B03_GetDataGridDTO.setRoleName(user.getRole().getRoleName());
            B03_GetDataGridDTO.setName(user.getFirstName() + " " + user.getLastName());
            B03_GetDataGridDTO.setDescription(user.getDescription());
            B03_GetDataGridDTO.setPhone(user.getPhone());
            B03_GetDataGridDTO.setStatus(user.getStatus());
            B03_GetDataGridDTO.setCreateOn(new Date(user.getCreateAt() * 1000L));
            B03_GetDataGridDTO.setUsername(user.getUsername());
            B03_GetDataGridDTO.setAddress(user.getLocation().getAddress());
            B03_GetDataGridDTO.setCountry(user.getLocation().getCountry());
            B03_GetDataGridDTO.setDistrict(user.getLocation().getDistrict());
            B03_GetDataGridDTO.setWard(user.getLocation().getWard());
            B03_GetDataGridDTO.setCity(user.getLocation().getCity());
            B03_GetDataGridDTO.setOrgName(user.getOrg_name());
            return B03_GetDataGridDTO;
        });

        return ResponseEntity.ok(B03_GetDataGridDTOPage);
    }



    public ResponseEntity<Role> getRoleByUserId(int userId) {
        User user = userRepository.findOneByUserId(userId);
        if (user != null) {
            Role role = user.getRole();
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    public ResponseEntity<String> updateCertification(String otp) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        if (currentUser.getStatus() != 0)
//        {
//            return ResponseEntity.ok("The commitment contract already singed");
//        }else{
//            UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);
//            String finalHtml;
//
//            DataMailDTO dataMail = new DataMailDTO();
//            LocalDate currentDate = LocalDate.now();
//
//            Map<String, Object> props = new HashMap<>();
//            props.put("companyName", userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName());
//            props.put("companyAddress", userProfileDTO.getAddress());
//            props.put("phoneNumber", userProfileDTO.getPhone());
//            props.put("email", userProfileDTO.getEmail());
//            props.put("day", currentDate.format(DateTimeFormatter.ofPattern("dd")));
//            props.put("month", currentDate.format(DateTimeFormatter.ofPattern("MM")));
//            props.put("year", currentDate.format(DateTimeFormatter.ofPattern("yyyy")));
//            props.put("signed", true);
//            props.put("signerName", userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName());
//            props.put("signDate", currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//
//            dataMail.setProps(props);
//
//            Context context = new Context();
//            context.setVariables(dataMail.getProps());
//
//            finalHtml = springTemplateEngine.process(Const.TEMPLATE_FILE_NAME_eSgin.ESGIN, context);
//
//            byte[] pdfBytes = documentGenerator.onlineHtmlToPdf(finalHtml);
//            if (pdfBytes != null) {
//                Certificate certificate = new Certificate();
//                certificate.setCertificateName("Test");
//                certificate.setIssuingAuthority("test");
//                certificate.setImages(pdfBytes);
//                certificate.setIssuanceDate(System.currentTimeMillis());
//                certificate.setManufacturer(currentUser);
//                certificateRepository.save(certificate);
//                currentUser.setStatus(1);
//                userRepository.save(currentUser);
//            }
//            return ResponseEntity.ok("Singed");
//        }
//    }


    public ResponseEntity<?> updateStatus(int userId, int status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);
//        if (userProfileDTO.getRole().getRoleId() != 1) {
//            return ResponseEntity.ok(null);
//        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(status);
            userRepository.save(user);
            return ResponseEntity.ok(userProfileDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    public Boolean lockUser(String req) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);

            if (userProfileDTO == null || userProfileDTO.getRole().getRoleId() != 1) {
                return false;
            }

            JSONObject jsonObject = new JSONObject(req);
            int userId = jsonObject.has("userId") ? jsonObject.getInt("userId") : -1;
            int status = jsonObject.has("status") ? jsonObject.getInt("status") : -1;

            if (status != 1 & status != 2) {
                return false;
            }

            boolean isLock = (status == 1);
            String subject = isLock ? Const.SEND_MAIL_SUBJECT.SUBJECT_LOCKUSER : Const.SEND_MAIL_SUBJECT.SUBJECT_UNLOCKUSER;
            String template = isLock ? Const.TEMPLATE_FILE_NAME.LOCKUSER_DETAIL : Const.TEMPLATE_FILE_NAME.UNLOCKUSER_DETAIL;

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return false;
            }
            User user = userOptional.get();

            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo(user.getEmail());
            dataMail.setSubject(subject);

            Map<String, Object> props = new HashMap<>();
            props.put("name", user.getFirstName() + " " + user.getLastName());
            props.put("username", user.getUsername());
            dataMail.setProps(props);

            mailService.sendHtmlMail(dataMail, template);

            // Update user status
            user.setStatus(status);
            userRepository.save(user);

            return true;
        } catch (JSONException | MessagingException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }

//    public Boolean warningUser(String req) {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);
//
//            if (userProfileDTO == null || userProfileDTO.getRole().getRoleId() != 1) {
//                return false;
//            }
//
//            JSONObject jsonObject = new JSONObject(req);
//            int userId = jsonObject.has("userId") ? jsonObject.getInt("userId") : -1;
//            int status = jsonObject.has("status") ? jsonObject.getInt("status") : -1;
//
//            if (status != 1 && status != 2) { // Changed & to && for logical AND
//                return false;
//            }
//
//            String subject = Const.SEND_MAIL_SUBJECT.SUBJECT_WARNING; // Changed subject
//            String template = Const.TEMPLATE_FILE_NAME.REPLY_REPLY_WARNNING_USER; // Changed template
//
//            Optional<User> userOptional = userRepository.findById(userId);
//            if (userOptional.isEmpty()) {
//                return false;
//            }
//            User user = userOptional.get();
//
//            DataMailDTO dataMail = new DataMailDTO();
//            dataMail.setTo(user.getEmail());
//            dataMail.setSubject(subject);
//
//            Map<String, Object> props = new HashMap<>();
//            props.put("name", user.getFirstName() + " " + user.getLastName());
//            dataMail.setProps(props);
//            mailService.sendHtmlMail(dataMail, template);
//
//            // Update user status
//            user.setStatus(status);
//            userRepository.save(user);
//
//            return true;
//        } catch (JSONException | MessagingException | NullPointerException | NumberFormatException e) {
//            return false;
//        }
//    }
//

    //Update Table
    public ResponseEntity<String> updateUserDescriptions(List<B03_GetDataGridDTO> userUpdateRequests) {
        for (B03_GetDataGridDTO updateRequest : userUpdateRequests) {
            Optional<User> optionalUser = userRepository.findById(updateRequest.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setDescription(updateRequest.getDescription());
                userRepository.save(user);
            } else {
                return ResponseEntity.badRequest().body("User with ID " + updateRequest.getUserId() + " not found.");
            }
        }
        return ResponseEntity.ok("User descriptions updated successfully.");
    }

    //get Role
    public ResponseEntity<Role> getAllUser(int userId) {
        User user = userRepository.findOneByUserId(userId);
        if (user != null) {
            Role role = user.getRole();
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<UserProfileDTO> getUserByUserID(String req) {
        JSONObject jsonReq = new JSONObject(req);
        int userId = jsonReq.has("userId") ? jsonReq.getInt("userId") : -1;
        UserProfileDTO currentUser;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (userId > -1 || !(authentication.getPrincipal() instanceof User)) {
            currentUser = getUserProfile(authentication, userId);
            return ResponseEntity.ok(currentUser);
        }

        UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);

        if (userProfileDTO.getRole().getRoleId() != 1) {
            return ResponseEntity.ok(userProfileDTO);
        } else if (userProfileDTO.getRole().getRoleId() == 1) {
            currentUser = getUserProfile(authentication, userId);
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }


    public ResponseEntity generateDoc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfileDTO userProfileDTO = getUserProfile(authentication, -1);
        //kiem tra user ton tai va da ky hop dong chua
        if (userProfileDTO != null && userProfileDTO.getStatus() == 0) {
            String finalHtml;

            DataMailDTO dataMail = new DataMailDTO();
            LocalDate currentDate = LocalDate.now();

            Map<String, Object> props = new HashMap<>();
            props.put("companyName", userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName());
            props.put("companyAddress", userProfileDTO.getAddress());
            props.put("phoneNumber", userProfileDTO.getPhone());
            props.put("email", userProfileDTO.getEmail());
            props.put("day", currentDate.format(DateTimeFormatter.ofPattern("dd")));
            props.put("month", currentDate.format(DateTimeFormatter.ofPattern("MM")));
            props.put("year", currentDate.format(DateTimeFormatter.ofPattern("yyyy")));
            props.put("signed", false);
            dataMail.setProps(props);

            Context context = new Context();
            context.setVariables(dataMail.getProps());

            finalHtml = springTemplateEngine.process(Const.TEMPLATE_FILE_NAME_eSgin.ESGIN, context);

            byte[] pdfBytes = documentGenerator.onlineHtmlToPdf(finalHtml);
            if (pdfBytes == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } else if (userProfileDTO != null && userProfileDTO.getStatus() != 0) {
            User currentUser = (User) authentication.getPrincipal();
            Certificate certificate = certificateRepository.findOneByManufacturer_userId(currentUser.getUserId());
            if (certificate != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf");
                headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
                return ResponseEntity.ok().headers(headers).body(certificate.getImages());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(400).body(null);
        }

    }

    public ResponseEntity<String> updateAvatar(String req) {
        try {
            JSONObject jsonReq = new JSONObject(req);
            String file = jsonReq.has("file") ? jsonReq.getString("file") : "";
            if (file.isEmpty())
            {
                return ResponseEntity.status(500).body("Image is null");
            }
            //input vào 1 file
            //gọi service upload: In: file | Out: Key của ảnh
            MultipartFile fileCore = cloudinaryService.convertBase64ToImgFile(file);
            String url = cloudinaryService.uploadImageAndGetPublicId(fileCore, "");
            User user;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication.getPrincipal() instanceof User)) {
                user = (User) authentication.getPrincipal();
                //save key cua ảnh vào database
                user.setProfileImage(url);
                userRepository.save(user);
            }
            return ResponseEntity.status(200).body("Avatar update success");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    public ResponseEntity<String> updateOrgImage(String req) {
        try {
            JSONObject jsonReq = new JSONObject(req);
            String file = jsonReq.has("file") ? jsonReq.getString("file") : "";
            if (file.isEmpty())
            {
                return ResponseEntity.status(500).body("Image is null");
            }
            //input vào 1 file
            //gọi service upload: In: file | Out: Key của ảnh
            MultipartFile fileCore = cloudinaryService.convertBase64ToImgFile(file);
            String url = cloudinaryService.uploadImageAndGetPublicId(fileCore, "");
            User user;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication.getPrincipal() instanceof User)) {
                user = (User) authentication.getPrincipal();
                //save key cua ảnh vào database
                user.setOrgImage(url);
                userRepository.save(user);
            }
            return ResponseEntity.status(200).body("OrgImage update success");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    public ResponseEntity<String> updateDescription(String req) {
            JSONObject jsonReq = new JSONObject(req);
            String description = jsonReq.has("description") ? jsonReq.getString("description") : "";
            User user;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication.getPrincipal() instanceof User)) {
                user = (User) authentication.getPrincipal();
                user.setDescription(description);
                userRepository.save(user);
            }
            return ResponseEntity.status(200).body("description update success");
    }



    public ResponseEntity<?> countRegisteredUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.status(200).body(users.size());

    }

    public ResponseEntity<?> listAllCustomerSupport(FilterSearchSupporterRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            try {
                Page<User> users;
                Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
                        req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
                                PageRequest.of(req.getPageNumber(), req.getPageSize());
                users = userRepository.findAllSupport("%" + req.getEmail() + "%", pageable);
                return ResponseEntity.status(200).body(users.map(userMapper::usersToSupporterDTOResponse));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error when fetching data: "+e.toString());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("require administrator for this action");
    }
    public ResponseEntity<?> deleteSupporter(IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            try {
                authTokenRepository.deleteOneById(req.getId());
                userRepository.deleteById(req.getId());
                return ResponseEntity.status(200).body("delete successful account supporter");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error when fetching data");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("require administrator for this action");
    }
    public ResponseEntity<?> searchAllManufacturer() {
        List<User> users = userRepository.findAllManufacturer();
        return ResponseEntity.status(200).body(userMapper.usersToUserViewDTOs(users));
    }
    public JSONObject infoUserForMonitor(long startDate, long endDate) {
        List<User> monthlyUser = userRepository.findAllUserByCreateAtBetween(startDate, endDate);
        List<User> users = userRepository.findAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", users.size());
        jsonObject.put("monthly",monthlyUser.size());
        return jsonObject;
    }
    public List<OrgNameUserDTO> getTop5OrgNames() {
            try {
                Pageable topFive = PageRequest.of(0, 5);
                List<OrgNameUserDTO> dtoList = itemRepository.findTop5OrgNames(topFive);
                List<OrgNameUserDTO> result = new ArrayList<>();
                for (OrgNameUserDTO item : dtoList) {
                    item.setUserImage(cloudinaryService.getImageUrl(item.getUserImage()));
                    result.add(item);
                }
//                List<OrgNameUserDTO> dtoList = items.stream()
//                        .map(item -> new OrgNameUserDTO(item.getProduct().getManufacturer().getOrg_name(), item.getProduct().getManufacturer().getUserId(),
//                                item.getProduct().getManufacturer().getProfileImage()))
//                        .collect(Collectors.toList());

                return result;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
    }


}