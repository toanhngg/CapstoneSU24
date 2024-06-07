package fpt.CapstoneSU24.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
public class EmailController {

//
//    @Autowired
//    private ClientService clientService;
//    @Autowired
//    private ItemResponsitory itemResponsitory;
//    @Autowired
//    private EmailService emailService;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @PostMapping(value = "/create")
//    public ResponseEntity create(@RequestBody ClientSdi sdi, @RequestParam String ProductRecognition ) throws MessagingException {
//        //đoạn này dùng để xác thực xem mail người dùng có đúng mail ợc ủy quyền không
//        // sau đó generate mã OTP r send mail
//        //User authenticatedUser = authenticationService.authenticate(email, password);
//        //String jwtToken = jwtService.generateToken(authenticatedUser);
//        //return ResponseEntity.ok(clientService.create(sdi));
//        Item item = itemResponsitory.findByProductRecognition(ProductRecognition);
//
//        if(sdi.getEmail() != null ) {
//            // Xác nhận xem item này có đang được ủy quyền không
//            if ((sdi.getEmail()).equals(item.getCurrentOwner())) {
//                return ResponseEntity.ok(clientService.creatAndSaveSQL(sdi));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not match! Import email again please!");
//            }
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This product has not been authorized!");
//
//        }
//    }
//
//    // người duùng nhập OTP ở đây hệ thống cf đúng thì trả về oke
//    @PostMapping(value = "/confirmOTP")
//    public ResponseEntity<Boolean> confirmOTP(@RequestBody SendOTP otp) {
//        //System.out.println("OTP" + otp.getOtp().toString());
//        //System.out.println("Email" + otp.getEmail().toString());
//
//        // luu sqlserver
//        boolean check = clientService.checkOTP(otp.getEmail(),otp.getOtp());
//        if(check){
//
//        }
//        // ---luwuu redis
//        //boolean check = clientService.checkOTP(OTPtoken);
//        return ResponseEntity.status(200).body(check);
//    }
}
