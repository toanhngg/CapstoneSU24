package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.Certificate.CreateCertificateRequest;
import fpt.CapstoneSU24.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/getListCertificate")
    public ResponseEntity<?> getListCertificate(@RequestBody String req) {
        return certificateService.getListCertificate(req);
    }


    @PutMapping("/createCertificate")
    public ResponseEntity<?> createCertificate(@RequestBody CreateCertificateRequest req)
    {
        return certificateService.createCertificate(req);
    }

}





