package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.EmailServiceRepository;
import fpt.CapstoneSU24.service.ClientService;
import fpt.CapstoneSU24.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/mail")
 public class EmailController {

//        @Autowired
//        private EmailServiceRepository emailService;
//
//        @GetMapping("/sendEmail")
//        public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
//           // emailService.sendSimpleEmail(to, subject, text);
//            return "Email sent successfully";
//        }
        @Autowired
        private ClientService clientService;
        @Autowired
        private EmailService emailService;
        @PostMapping(value = "/create")
        public ResponseEntity<Boolean> create(@RequestBody ClientSdi sdi) throws MessagingException {
            //đoạn này dùng để xác thực xem mail người dùng có đúng mail ợc ủy quyền không
            // sau đó generate mã OTP r send mail
            //User authenticatedUser = authenticationService.authenticate(email, password);
            //String jwtToken = jwtService.generateToken(authenticatedUser);
            return ResponseEntity.ok(clientService.create(sdi));
        }
        // người duùng nhập OTP ở đây hệ thống cf đúng thì trả về oke
    @PostMapping(value = "/confirmOTP")
    public ResponseEntity<Boolean> confirmOTP(String OTPtoken) {
        boolean check = clientService.confirmOTP(OTPtoken);
        return ResponseEntity.ok(check);
    }
    }
