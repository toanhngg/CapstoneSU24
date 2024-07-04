package fpt.CapstoneSU24.service;


import fpt.CapstoneSU24.controller.AuthenticationController;
import fpt.CapstoneSU24.dto.ChangePasswordDto;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.payload.VerifyEmailRequest;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.dto.payload.ForgotPasswordRequest;
import fpt.CapstoneSU24.dto.payload.LoginRequest;
import fpt.CapstoneSU24.dto.payload.RegisterRequest;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.util.Const;
import fpt.CapstoneSU24.util.DataUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.DateUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    AuthTokenRepository authTokenRepository;
    @Autowired
    private EmailService mailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private ClientService clientService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity signup(RegisterRequest input) {
        if (clientService.checkOTPinSQL(input.getEmail(), input.getOtpVerify())) {
            try {
                if (userRepository.findOneByEmail(input.getEmail()) == null) {
//                if (roleRepository.findOneByRoleId(2) == null) {
//                    roleRepository.save(new Role(0, "admin"));
//                    roleRepository.save(new Role(0, "manufacture"));
//                }
                    User user = new User();
                    user.setEmail(input.getEmail());
                    user.setFirstName(input.getFirstName());
                    user.setLastName(input.getLastName());
                    user.setPhone(input.getPhone());
                    user.setOrg_name(input.getOrgName());
                    user.setRole(roleRepository.findOneByRoleId(2));
                    user.setPassword(passwordEncoder.encode(input.getPassword()));
                    user.setCreateAt(System.currentTimeMillis());
                    Location location = new Location(0, input.getAddress(), input.getCity(), input.getCountry(), input.getCoordinateX(), input.getCoordinateY(), input.getDistrict(), input.getWard()); //manhDT sua bang
                    locationRepository.save(location);
                    user.setLocation(location);
                    userRepository.save(user);
                    authTokenRepository.save(new AuthToken(0, user, null));
                    return ResponseEntity.status(HttpStatus.OK).body("create successfully");
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("email already exists");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error create new account");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("otp code is incorrect");
    }

    public ResponseEntity login(LoginRequest loginRequest, HttpServletResponse response) {
        User authenticatedUser = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String jwtToken = jwtService.generateToken(authenticatedUser, authenticatedUser);
        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken) // key & value
                .secure(true).httpOnly(true)
                .path("/")
                .sameSite("None")
                .domain(null)
                .maxAge(-1)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("jwt: " + jwtToken);
        log.info("User {} is attempting to log in.", loginRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("login successfully");
    }

    public ResponseEntity sendEmailVerify(VerifyEmailRequest req) {
        try {
            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo(req.getEmail());
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.VERIFY_EMAIL);

            //create otp
            String otpVerify = DataUtils.generateTempPwd(6);
            Map<String, Object> props = new HashMap<>();
            props.put("otpVerify", otpVerify);

            dataMail.setProps(props);

            //expire otp
            Date expiryTime = otpService.calculateExpiryTime(2);
            OTP otpCheck = otpRepository.findOTPByEmail(req.getEmail());
            if (otpCheck == null) {
                otpCheck = new OTP(req.getEmail(), otpVerify, expiryTime);
                otpRepository.save(otpCheck);
            } else {
                otpService.updateOTPCode(req.getEmail(), otpVerify, expiryTime);
            }
            //send email
            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.VERIFY_EMAIL);
            return ResponseEntity.status(HttpStatus.OK).body("Send email successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity logout(HttpServletResponse response) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            AuthToken authToken = authTokenRepository.findOneById(currentUser.getUserId());
            if (authToken != null) {
                authToken.setJwtHash(null);
                authTokenRepository.save(authToken);
            }
            ResponseCookie cookie = ResponseCookie.from("jwt", null) // key & value
                    .secure(true).httpOnly(true)
                    .path("/")
                    .sameSite("None")
                    .domain(null)
                    .maxAge(0)
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.OK).body("logout successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("logout successfully");
        }
    }

    public ResponseEntity<String> changePassword(ChangePasswordDto changePasswordDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not authenticated");
            }

            User user = (User) authentication.getPrincipal();

            // Check if the new password and confirm password match
            if (!changePasswordDto.getPassword().equals(changePasswordDto.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("New password and confirm password do not match");
            }

            // Change password for the authenticated user
            user.setPassword(changePasswordDto.getPassword());
            ChangePassword(user);

            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<String> forgetPassword(ForgotPasswordRequest req) {
        try {
            JSONObject jsonNode = new JSONObject(req);
            String email = jsonNode.getString("email");
            User user = userRepository.findOneByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email incorrect !!!");
            } else {
                String rndPass = generateRandomPassword();
                user.setPassword(rndPass);
                ChangePassword(user);
                DataMailDTO dataMail = new DataMailDTO();

                dataMail.setTo(user.getEmail());

                dataMail.setSubject(Const.SEND_MAIL_SUBJECT.SUBJECT_CHANGEPASS);

                Map<String, Object> props = new HashMap<>();
                props.put("name", user.getFirstName() + user.getLastName());
                props.put("newPassword", rndPass);

                dataMail.setProps(props);

                mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CHANGEPASSWORD);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public User authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
        return userRepository.findByEmail(email)
                .orElseThrow();
    }

    public User ChangePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String generateRandomPassword() {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialCharacters = "@#$%^&+=!*()";
        String allCharacters = lowercase + uppercase + digits + specialCharacters;
        SecureRandom random = new SecureRandom();

        int length = 8 + random.nextInt(9);
        StringBuilder password = new StringBuilder(length);

        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return shuffleString(password.toString());
    }

    private static String shuffleString(String string) {
        SecureRandom random = new SecureRandom();
        char[] characters = string.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

}