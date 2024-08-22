package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.SearchSupportSystemByUserDTO;
import fpt.CapstoneSU24.dto.SearchSupportSystemDTO;
import fpt.CapstoneSU24.dto.payload.AddSupportRequest;
import fpt.CapstoneSU24.dto.payload.ReplySupportRequest;
import fpt.CapstoneSU24.mapper.SupportSystemMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.ImageSupportSystemRepository;
import fpt.CapstoneSU24.repository.SupportSystemRepository;
import fpt.CapstoneSU24.util.Const;
import jakarta.mail.MessagingException;
import org.json.JSONObject;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupportSystemService {
    private final CloudinaryService cloudinaryService;
    private final SupportSystemRepository supportSystemRepository;
    private final ImageSupportSystemRepository imageSupportSystemRepository;
    private final SupportSystemMapper supportSystemMapper;
    private final EmailService mailService;

    @Autowired
    public SupportSystemService(SupportSystemMapper supportSystemMapper,
                                SupportSystemRepository supportSystemRepository,
                                CloudinaryService cloudinaryService,
                                ImageSupportSystemRepository imageSupportSystemRepository,
                                EmailService  mailService) {
        this.supportSystemMapper = supportSystemMapper;
        this.cloudinaryService = cloudinaryService;
        this.supportSystemRepository = supportSystemRepository;
        this.imageSupportSystemRepository = imageSupportSystemRepository;
        this.mailService = mailService;
    }

     /*
     * reply = -1 => user không reply lại nữa*/
    public ResponseEntity<?> addSupport(AddSupportRequest req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2) {
            SupportSystem supportSystem = new SupportSystem();
            supportSystem.setUser(currentUser);
            supportSystem.setTitle(req.getTitle());
            supportSystem.setContent(req.getContent());
            supportSystem.setStatus(0);
            supportSystem.setReplyId(-1);
            supportSystem.setTimestamp(System.currentTimeMillis());
            supportSystemRepository.save(supportSystem);
            // add images
            if (!req.getImages().isEmpty()) {
                for (String obj : req.getImages()) {
                    String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj), "");
                    imageSupportSystemRepository.save(new ImageSupportSystem(0, filePath,0, supportSystem));
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("add new support by user successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not permit to access");
        }
    }

    public ResponseEntity<?> replyBySupporter(ReplySupportRequest req) throws IOException, MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() != 2) {
            SupportSystem supportSystem = supportSystemRepository.findOneBySupportSystemId(req.getId());
            if(supportSystem.getSupportContent() != null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("the answer exíst");
            SupportSystem updateStatus = supportSystemRepository.findOneBySupportSystemId(supportSystem.getReplyId());
            if(updateStatus != null){
                updateStatus.setStatus(1);
                supportSystemRepository.save(updateStatus);
            }
            supportSystem.setSupportContent(req.getContent());
            supportSystem.setSupporterName(currentUser.getLastName() + " " + currentUser.getFirstName());
            supportSystem.setStatus(1);
            supportSystem.setSupportTimestamp(System.currentTimeMillis());
            supportSystemRepository.save(supportSystem);

            // add images
            if (!req.getImages().isEmpty()) {
                for (String obj : req.getImages()) {
                    String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj), "");
                    imageSupportSystemRepository.save(new ImageSupportSystem(0, filePath,1, supportSystem));
                }
            }
            //gui mail o day
            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo(supportSystem.getUser().getEmail());
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.SUBJECT_REPLY_SUPPORT_USER);
            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.REPLY_SUPPORT_USER);
            return ResponseEntity.status(HttpStatus.OK).body("reply by supporter successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not permit to access");
        }
    }
    /*
     * reply = 1 => user reply lại lần nữa*/
    public ResponseEntity<?> replyByUser(ReplySupportRequest req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2) {
            SupportSystem supportSystem = supportSystemRepository.findOneBySupportSystemId(req.getId());
            SupportSystem replySupportSystem = new SupportSystem();
            replySupportSystem.setTitle(supportSystem.getTitle());
            replySupportSystem.setContent(req.getContent());
            replySupportSystem.setUser(currentUser);
            replySupportSystem.setTimestamp(System.currentTimeMillis());
            replySupportSystem.setReplyId(req.getId());
            supportSystem.setStatus(0);
            supportSystemRepository.save(replySupportSystem);
            supportSystemRepository.save(supportSystem);


            // add images
            if (!req.getImages().isEmpty()) {
                for (String obj : req.getImages()) {
                    String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj), "");
                    imageSupportSystemRepository.save(new ImageSupportSystem(0, filePath,0, replySupportSystem));
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("reply by user successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not permit to access");
        }
    }
    public ResponseEntity<?> listAllBySupporter(SearchSupportSystemDTO req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() != 2) {
            Pageable pageable = req.getType().equals("desc") ?
                    PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "timestamp")) :
                    PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "timestamp"));

            Page<SupportSystem> supportSystems;

            if (req.getStatus() == 3) {
                if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                    supportSystems = supportSystemRepository.searchSupportSystemWithDate(req.getKeyword(), req.getStartDate(), req.getEndDate(), pageable);
                } else {
                    supportSystems = supportSystemRepository.searchSupportSystem(req.getKeyword(), pageable);
                }
            } else {
                if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                    supportSystems = supportSystemRepository.searchSupportSystemWithDateAndStatus(req.getKeyword(), req.getStartDate(), req.getEndDate(), req.getStatus(), pageable);
                } else {
                    supportSystems = supportSystemRepository.searchSupportSystemWithStatus(req.getKeyword(), req.getStatus(), pageable);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(supportSystems.map(supportSystemMapper::supportSystemToListSupportDTOResponse));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not permit to access");
        }
    }
    public ResponseEntity<?> listAllByUser(SearchSupportSystemByUserDTO req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2) {
            Pageable pageable = req.getType().equals("desc") ?
                    PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "timestamp")) :
                    PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "timestamp"));
            Page<SupportSystem> supportSystems;
            if(req.getStatus() == 3){
                supportSystems = supportSystemRepository.searchSupportSystemByUser(currentUser.getUserId(), pageable);
            }else {
                supportSystems = supportSystemRepository.searchSupportSystemByUser(currentUser.getUserId(),req.getStatus(), pageable);
            }

            return ResponseEntity.status(HttpStatus.OK).body(supportSystems.map(supportSystemMapper::supportSystemToListSupportDTOResponse));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not permit to access");
        }
    }
    public ResponseEntity<?> countStatus() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() != 2) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("waiting", supportSystemRepository.findAllByStatusAndReplyId(0,-1).size());
                jsonObject.put("done", supportSystemRepository.findAllByStatusAndReplyId(1,-1).size());
                return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());

            } catch (Exception ex) {
                return null;
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not permit to access");
        }
    }
}
