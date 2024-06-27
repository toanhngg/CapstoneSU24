package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.Certificate.CertificateListDTO;
import fpt.CapstoneSU24.dto.Certificate.CreateCertificateRequest;
import fpt.CapstoneSU24.dto.ListCertificateDTOResponse;
import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.model.Certificate;
import fpt.CapstoneSU24.model.User;
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
    EpochDate epochDate;

    @Autowired
    UserService userService;

    @PostMapping("/getListCertificate")
    public ResponseEntity<?> getListCertificate(@RequestBody String req)
    {
        JSONObject jsonReq = new JSONObject(req);
        int userId = jsonReq.has("userId") ? jsonReq.getInt("userId") : -1;
        if (userId < 1)
        {
            return new ResponseEntity<>("UserId not valid", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Certificate> certificate = certificateRepository.findByManufacturer_userId(userId);

        List<CertificateListDTO> listCertificate = new ArrayList<>();
        for (Certificate certificate1 : certificate) {
            CertificateListDTO certificateListDTO;
            certificateListDTO = mapper_CertificateDTO(certificate1);
            listCertificate.add(certificateListDTO);
        }
        return ResponseEntity.ok(listCertificate);
    }





    private CertificateListDTO mapper_CertificateDTO(Certificate certificate){
        CertificateListDTO certificateListDTO = new CertificateListDTO();
        certificateListDTO.setCertificateName(certificate.getCertificateName());
        certificateListDTO.setCertificateImage("temp");
        String issuanceDate = epochDate.dateTimeToString(epochDate.epochToDate(certificate.getIssuanceDate()), "dd-MM-yyyy");
        certificateListDTO.setIssuanceDate(issuanceDate);
        certificateListDTO.setIssuing_authority(certificate.getIssuingAuthority());
        certificateListDTO.setManufacturerName(certificate.getManufacturer().getFirstName() + certificate.getManufacturer().getLastName());
        certificateListDTO.setCertificateId(String.valueOf(certificate.getCertificateId()));
        certificateListDTO.setManufacturerId(String.valueOf(certificate.getManufacturer().getUserId()));
        return certificateListDTO;
    }

    @PutMapping("/createCertificate")
    public ResponseEntity<?> createCretificate(@RequestBody CreateCertificateRequest req)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof User user))
        {
            return new ResponseEntity<>("You need login first", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Certificate newCertificate = new Certificate();
        newCertificate.setCertificateName(req.getName());
        newCertificate.setImage(null); //temp\
        newCertificate.setIssuanceDate(epochDate.dateToEpoch(req.issuanceDate));
        newCertificate.setIssuingAuthority(req.getIssuanceAuthority());
        newCertificate.setManufacturer(user);

        certificateRepository.save(newCertificate);
        return new ResponseEntity<>("true", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}





