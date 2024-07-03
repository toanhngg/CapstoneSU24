package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.Certificate.CreateCertificateRequest;
import fpt.CapstoneSU24.dto.ListCertificateDTOResponse;
import fpt.CapstoneSU24.dto.payload.ListManuToVerifyRequest;
import fpt.CapstoneSU24.dto.payload.ReplyCertByAdminRequest;
import fpt.CapstoneSU24.mapper.UserMapper;
import fpt.CapstoneSU24.model.Certificate;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.dto.payload.IdRequest;
import fpt.CapstoneSU24.repository.CertificateRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final EpochDate epochDate;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public CertificateService(CertificateRepository certificateRepository,
                              UserRepository userRepository,
                              CloudinaryService cloudinaryService, EpochDate epochDate, UserMapper userMapper) {
        this.certificateRepository = certificateRepository;
        this.epochDate = epochDate;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public ResponseEntity<?> getListCertificateByManuId(IdRequest req) {
        try {
            List<Certificate> certificates = certificateRepository.findByManufacturer_userId(req.getId());
            List<ListCertificateDTOResponse> certificateListDTOList = new ArrayList<>();
            for (Certificate c : certificates) {
                String[] parts = c.getImages().split("\\.");
                List<String> list = new ArrayList<>();
                for (String part : parts) {
                    list.add(cloudinaryService.getImageUrl(part));
                }
                certificateListDTOList.add(new ListCertificateDTOResponse( c.getCertificateId(),c.getCertificateName(), c.getIssuingAuthority(), list, c.getIssuanceDate()));
            }

            return ResponseEntity.ok(certificateListDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("id is not exist");
        }
    }

    public ResponseEntity<?> replyCertByAdmin(ReplyCertByAdminRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            try {
                if (req.getIsAccept() == 1) {
                    int n = certificateRepository.updateCertificateNoteByManufacturerId(req.getManufacturerId(), req.getNote());
                    User user = userRepository.findOneByUserId(req.getManufacturerId());
                    user.setStatus(1);
                    userRepository.save(user);
                } else {
                    int n = certificateRepository.updateCertificateNoteByManufacturerId(req.getManufacturerId(), req.getNote());
                    User user = userRepository.findOneByUserId(req.getManufacturerId());
                    user.setStatus(7);
                    userRepository.save(user);
                }
                return ResponseEntity.ok("update status for manufacturer id " + req.getManufacturerId());
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error");
            }
        }
        return ResponseEntity.status(500).body("your account is not allowed for this action");
    }

    public ResponseEntity<?> deleteAllCertByManufacturer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2 && currentUser.getStatus() == 7) {
            try {
                certificateRepository.deleteAllByManufacturer_userId(currentUser.getUserId());
                return ResponseEntity.status(200).body("deleted all successfully");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error");
            }
        }
        return ResponseEntity.status(500).body("your account is not allowed for this action");
    }
    public ResponseEntity<?> deleteCertCertId(IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2 && currentUser.getStatus() == 7) {
            try {
                Certificate c = certificateRepository.findOneByCertificateId(req.getId());
                if(c != null){
                    User certUser = c.getManufacturer();
                    if(certUser.getUserId() == currentUser.getUserId())
                    {
                        certificateRepository.deleteAllByCertificateId(req.getId());
                        return ResponseEntity.status(200).body("deleted all successfully");
                    }
                    return ResponseEntity.status(500).body("the certificateId isn't your");
                }
                return ResponseEntity.status(500).body("certificateId = "+ req.getId() + " not exist");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error");
            }
        }
        return ResponseEntity.status(500).body("your account is not allowed for this action");
    }

    public ResponseEntity<?> getListCertMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2) {
            try {
                List<Certificate> certificates = certificateRepository.findByManufacturer_userId(currentUser.getUserId());
                List<ListCertificateDTOResponse> certificateListDTOList = new ArrayList<>();
                for (Certificate c : certificates) {
                    String[] parts = c.getImages().split("\\.");
                    List<String> list = new ArrayList<>();
                    for (String part : parts) {
                        list.add(cloudinaryService.getImageUrl(part));
                    }
                    certificateListDTOList.add(new ListCertificateDTOResponse(c.getCertificateId(), c.getCertificateName(), c.getIssuingAuthority(), list, c.getIssuanceDate()));
                }

                return ResponseEntity.ok(certificateListDTOList);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error");
            }
        }
        return ResponseEntity.status(500).body("your account is not allowed for this action");
    }
    public ResponseEntity<?> sendRequestVerifyCert() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2 && currentUser.getStatus() != 1) {
            try {
              currentUser.setStatus(8);
              userRepository.save(currentUser);
                return ResponseEntity.ok("send request successful, waiting for admin approval");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error");
            }
        }
        return ResponseEntity.status(500).body("your account is not allowed for this action");
    }

    public ResponseEntity<?> getListManuToVerify(ListManuToVerifyRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            try {
                Page<User> users;
                Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
                        req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
                                PageRequest.of(req.getPageNumber(), req.getPageSize());
                if (!req.getPhone().isEmpty()) {
                    users = userRepository.findAllByPhoneContainingAndStatus(req.getPhone(), 8, pageable);
                } else {
                    users = userRepository.findAllByStatus(8, pageable);
                }
                return ResponseEntity.status(200).body(users.map(userMapper::usersToUserDTOs));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error when fetching data");
            }
        }
        return ResponseEntity.status(500).body("your account is not allowed for this action");
    }

    public ResponseEntity<?> createCertificate(CreateCertificateRequest req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2 && currentUser.getStatus() != 1) {
            try {
                String fullFilePath = "";
                for (int i = 0; i < req.getFile().size(); i++) {
                    MultipartFile file = cloudinaryService.convertBase64ToImgFile(req.getFile().get(i));
                    String fileName = cloudinaryService.uploadImageAndGetPublicId(file, "");
                    // if we need more cert => add "." at between them like "abc123.dcf123" (2 abc123, dcf123 => 2 images)
                    fullFilePath += (i == req.getFile().size() - 1) ? fileName : fileName + ".";
                }
                Certificate newCertificate = new Certificate();
                newCertificate.setCertificateName(req.getName());
                newCertificate.setImages(fullFilePath);
                newCertificate.setIssuanceDate(req.issuanceDate);
                newCertificate.setIssuingAuthority(req.getIssuanceAuthority());
                newCertificate.setManufacturer(currentUser);
                certificateRepository.save(newCertificate);
                currentUser.setStatus(7);
                userRepository.save(currentUser);
            } catch (Exception e) {
                return new ResponseEntity<>("error when create new certificate", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("your account is not allowed for this action", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("create certificate successfully", HttpStatus.OK);
    }
}