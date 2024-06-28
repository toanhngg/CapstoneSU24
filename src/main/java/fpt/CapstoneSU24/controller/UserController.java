package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.B03.B03_GetDataGridDTO;
import fpt.CapstoneSU24.dto.B03.B03_RequestDTO;
import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/getAllUser")
    public ResponseEntity getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/getDataToTable")
    public ResponseEntity<?> getUsersByEmail(@RequestBody B03_RequestDTO userRequestDTO) {
      return userService.getUsersByEmail(userRequestDTO);
    }


    @PutMapping("/lockUser")
    public ResponseEntity<String> updateStatus(@RequestBody String req) {
  return userService.updateStatus(req);
    }


    @PostMapping("/lockUser")
    public Boolean lockUser(@RequestBody String req) {
        return userService.lockUser(req);
    }



    //Update Table
    @PutMapping("/updateUserDescriptions")
    public ResponseEntity<String> updateUserDescriptions(@RequestBody List<B03_GetDataGridDTO> userUpdateRequests) {
      return userService.updateUserDescriptions(userUpdateRequests);
    }

    //get Role
    @GetMapping("/getRoleByUserId")
    public ResponseEntity<Role> getAllUser(@RequestParam int userId) {
       return userService.getAllUser(userId);
    }

    @PostMapping("/getUserById")
    public ResponseEntity<UserProfileDTO> getUserByUserID(@RequestBody String req)  {
       return userService.getUserByUserID(req);
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
       return userService.authenticatedUser();
    }



    @PostMapping("/getContract")
    public ResponseEntity generateDoc() {
       return userService.generateDoc();
    }

//    @PostMapping("/updateCertification")
//    public ResponseEntity<String> updateCertification(String otp) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        if (currentUser.getStatus() != 0)
//        {
//            return ResponseEntity.ok("The commitment contract already singed");
//        }else{
//            UserProfileDTO userProfileDTO = userService.getUserProfile(authentication, -1);
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
//
//    }

    @PutMapping("/updateAvatar")
    public ResponseEntity<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        return userService.updateAvatar(file);
    }
}





