package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.Certificate.CertificateListDTO;
import fpt.CapstoneSU24.dto.Certificate.CreateCertificateRequest;
import fpt.CapstoneSU24.dto.ListCertificateDTOResponse;
import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.mapper.UserMapper;
import fpt.CapstoneSU24.model.Certificate;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.CertificateRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.EpochDate;
import fpt.CapstoneSU24.service.UserService;
import fpt.CapstoneSU24.util.CloudinaryService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {
    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/getListCertificateByManuId")
    public ResponseEntity<?> getListCertificateByManuId(@Valid @RequestBody IdRequest req)
    {
        try {
            List<Certificate> certificates = certificateRepository.findByManufacturer_userId(req.getId());
            List<ListCertificateDTOResponse> certificateListDTOList = new ArrayList<>();
            for(Certificate c : certificates){
                String[] parts = c.getImages().split("\\.");
                List<String> list = Arrays.asList(parts);
                certificateListDTOList.add(new ListCertificateDTOResponse(c.getCertificateName(), c.getIssuingAuthority(), list, c.getIssuanceDate()));
            }

                return ResponseEntity.ok(certificateListDTOList);
        }catch (Exception e){
            return ResponseEntity.status(500).body("id is not exist");
        }
    }

    @GetMapping("/getListManuToVerify")
    public ResponseEntity<?> getListManuToVerify()
    {
        try {
            List<User> users = userRepository.findAllByStatus(0);
            return ResponseEntity.ok(UserMapper.INSTANCE.usersToUserDTOs(users));
        }catch (Exception e){
            return ResponseEntity.status(500).body("error when get all manu to verify");
        }
    }
//    private CertificateListDTO mapper_CertificateDTO(Certificate certificate){
//        CertificateListDTO certificateListDTO = new CertificateListDTO();
//        certificateListDTO.setCertificateName(certificate.getCertificateName());
//        certificateListDTO.setCertificateImage("temp");
//        String issuanceDate = epochDate.dateTimeToString(epochDate.epochToDate(certificate.getIssuanceDate()), "dd-MM-yyyy");
//        certificateListDTO.setIssuanceDate(issuanceDate);
//        certificateListDTO.setIssuing_authority(certificate.getIssuingAuthority());
//        certificateListDTO.setManufacturerName(certificate.getManufacturer().getFirstName() + certificate.getManufacturer().getLastName());
//        certificateListDTO.setCertificateId(String.valueOf(certificate.getCertificateId()));
//        certificateListDTO.setManufacturerId(String.valueOf(certificate.getManufacturer().getUserId()));
//        return certificateListDTO;
//    }

    @PostMapping("/createCertificate")
    public ResponseEntity<?> createCertificate(@Valid @RequestBody CreateCertificateRequest req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 2){
            try {
            String fullFilePath = "";
            for (int i = 0; i < req.getFile().size(); i++) {
                MultipartFile file = cloudinaryService.convertBase64ToImgFile(req.getFile().get(i));
                String fileName = cloudinaryService.uploadImageAndGetPublicId(file,"");
                // if we need more cert => add "." at between them like "abc123.dcf123" (2 abc123, dcf123 => 2 images)
                fullFilePath += (i == req.getFile().size() - 1) ? fileName : fileName +".";
            }
            Certificate newCertificate = new Certificate();
            newCertificate.setCertificateName(req.getName());
            newCertificate.setImages(fullFilePath);
            newCertificate.setIssuanceDate(req.issuanceDate);
            newCertificate.setIssuingAuthority(req.getIssuanceAuthority());
            newCertificate.setManufacturer(currentUser);
            certificateRepository.save(newCertificate);
            }catch (Exception e){
                return new ResponseEntity<>("error when create new certificate", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else {
            return new ResponseEntity<>("your account is not allowed for this action", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("create certificate successfully", HttpStatus.OK);
    }
}





