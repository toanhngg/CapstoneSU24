package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.SendOTP;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.service.ClientService;
import fpt.CapstoneSU24.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private CategoryRepository categoryRepository;
    @PostMapping(value = "/create")
    public ResponseEntity<Boolean> create(@RequestBody ClientSdi sdi) throws MessagingException {
        //đoạn này dùng để xác thực xem mail người dùng có đúng mail ợc ủy quyền không
        // sau đó generate mã OTP r send mail
        //User authenticatedUser = authenticationService.authenticate(email, password);
        //String jwtToken = jwtService.generateToken(authenticatedUser);
        //return ResponseEntity.ok(clientService.create(sdi));

        return ResponseEntity.ok(clientService.creatAndSaveSQL(sdi));
    }

    // người duùng nhập OTP ở đây hệ thống cf đúng thì trả về oke
    @PostMapping(value = "/confirmOTP")
    public ResponseEntity<Boolean> confirmOTP(@RequestBody SendOTP otp) {
        //System.out.println("OTP" + otp.getOtp().toString());
        //System.out.println("Email" + otp.getEmail().toString());

        // luu sqlserver
        boolean check = clientService.checkOTP(otp.getEmail(),otp.getOtp());
        if(check){

        }
        // ---luwuu redis
        //boolean check = clientService.checkOTP(OTPtoken);
        return ResponseEntity.status(200).body(check);
    }
}
