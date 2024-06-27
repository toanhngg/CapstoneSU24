package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.Certificate.CertificateListDTO;
import fpt.CapstoneSU24.dto.Certificate.CreateCertificateRequest;
import fpt.CapstoneSU24.model.Certificate;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CertificateRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final  EpochDate epochDate;
    @Autowired
    public CertificateService(CertificateRepository certificateRepository, EpochDate epochDate){
        this.certificateRepository = certificateRepository;
        this.epochDate = epochDate;
    }

    public ResponseEntity<?> getListCertificate(String req) {
        try {
            JSONObject jsonReq = new JSONObject(req);
            int userId = jsonReq.has("userId") ? jsonReq.getInt("userId") : -1;

            if (userId < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserId not valid");
            }

            List<Certificate> certificates = certificateRepository.findByManufacturer_userId(userId);
            List<CertificateListDTO> listCertificate = new ArrayList<>();

            for (Certificate certificate : certificates) {
                CertificateListDTO certificateListDTO = mapper_CertificateDTO(certificate);
                listCertificate.add(certificateListDTO);
            }

            return ResponseEntity.ok(listCertificate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
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

    public ResponseEntity<?> createCertificate(CreateCertificateRequest req) {
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
