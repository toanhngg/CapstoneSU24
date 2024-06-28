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
import fpt.CapstoneSU24.service.CertificateService;
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
    CertificateService certificateService;
    @PostMapping("/getListCertificateByManuId")
    public ResponseEntity<?> getListCertificateByManuId(@Valid @RequestBody IdRequest req)
    {
     return certificateService.getListCertificateByManuId(req);
    }

    @GetMapping("/getListManuToVerify")
    public ResponseEntity<?> getListManuToVerify()
    {
        return certificateService.getListManuToVerify();
    }

    @PostMapping("/createCertificate")
    public ResponseEntity<?> createCertificate(@Valid @RequestBody CreateCertificateRequest req) throws IOException {
        return certificateService.createCertificate(req);
    }
}





