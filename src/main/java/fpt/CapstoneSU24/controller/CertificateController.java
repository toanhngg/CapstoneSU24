package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.Certificate.CreateCertificateRequest;
import fpt.CapstoneSU24.dto.payload.IdRequest;
import fpt.CapstoneSU24.dto.payload.PhoneRequest;
import fpt.CapstoneSU24.dto.payload.ReplyCertByAdminRequest;
import fpt.CapstoneSU24.service.CertificateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {
    private final CertificateService certificateService;
    @Autowired
public CertificateController( CertificateService certificateService){
        this.certificateService = certificateService;
    }

    @PostMapping("/getListCertificateByManuId")
    public ResponseEntity<?> getListCertificateByManuId(@Valid @RequestBody IdRequest req)
    {
        return certificateService.getListCertificateByManuId(req);
    }

    @PostMapping("/getListManuToVerify")
    public ResponseEntity<?> getListManuToVerify(@Valid @RequestBody PhoneRequest req)
    {
        return certificateService.getListManuToVerify(req);
    }
    @GetMapping("/getListCertMe")
    public ResponseEntity<?> getListCertMe()
    {
        return certificateService.getListCertificateMe();
    }
    @GetMapping("/replyCertByAdmin")
    public ResponseEntity<?> replyCertByAdmin(@Valid @RequestBody ReplyCertByAdminRequest req)
    {
        return certificateService.replyCertByAdmin(req);
    }

    @PostMapping("/createCertificate")
    public ResponseEntity<?> createCertificate(@Valid @RequestBody CreateCertificateRequest req) throws IOException {
        return certificateService.createCertificate(req);
    }
}