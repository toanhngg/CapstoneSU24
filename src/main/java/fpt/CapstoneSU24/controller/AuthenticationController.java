package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ChangePasswordDto;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.RegisterRequest;
import fpt.CapstoneSU24.repository.AuthTokenRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.AuthenticationService;
import fpt.CapstoneSU24.service.EmailService;
import fpt.CapstoneSU24.service.JwtService;
import fpt.CapstoneSU24.payload.LoginRequest;
import fpt.CapstoneSU24.util.Const;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private EmailService mailService;
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody RegisterRequest registerRequest) {
        if(userRepository.findOneByEmail(registerRequest.getEmail()) == null){
            try {
                User registeredUser = authenticationService.signup(registerRequest);
                authTokenRepository.save(new AuthToken(0,registeredUser,null));
                return ResponseEntity.status(200).body("create successfully");
            }catch (Exception e){
                return ResponseEntity.ok().body(e);
            }
        }
        return  ResponseEntity.status(500).body("your email already exists");
    }
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User authenticatedUser = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String jwtToken = jwtService.generateToken(authenticatedUser, authenticatedUser);
        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken) // key & value
                .secure(false).httpOnly(true)
                .path("/")
                .sameSite("None")
                .domain(null)
                .maxAge(-1)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("jwt: " + jwtToken);
        log.info("User {} is attempting to log in.", loginRequest.getEmail());
        return ResponseEntity.status(200).body("login successfully");
    }
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User currentUser = (User) authentication.getPrincipal();
            if (currentUser != null) {
                AuthToken authToken = authTokenRepository.findOneById(currentUser.getUserId());
                if (authToken != null) {
                    authToken.setJwtHash(null);
                    authTokenRepository.save(authToken);
                }
                try {
                    ResponseCookie cookie = ResponseCookie.from("jwt", null) // key & value
                            .secure(false).httpOnly(true)
                            .path("/")
                            .sameSite("None")
                            .domain(null)
                            .maxAge(0)
                            .build();
                    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                    return ResponseEntity.status(200).body("logout successfully");
                } catch (Exception e) {
                    return ResponseEntity.ok().body(e);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(200).body("logout successfully");
        }
        return ResponseEntity.status(200).body("logout successfully");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            User user = (User) authentication.getPrincipal();

            // Check if the new password and confirm password match
            if (!changePasswordDto.getPassword().equals(changePasswordDto.getConfirmPassword())) {
                return ResponseEntity.status(400).body("New password and confirm password do not match");
            }

            // Change password for the authenticated user
            user.setPassword(changePasswordDto.getPassword());
            authenticationService.ChangePassword(user);

            return ResponseEntity.status(200).body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody String req) {
        try {
            JSONObject jsonNode = new JSONObject(req);
            String email = jsonNode.getString("email");
            User user = userRepository.findOneByEmail(email);
            if (user == null) {
                return ResponseEntity.status(401).body("Email incorrect !!!");
            }else{
                String rndPass = authenticationService.generateRandomPassword();
                user.setPassword(rndPass);
                authenticationService.ChangePassword(user);
                DataMailDTO dataMail = new DataMailDTO();

                dataMail.setTo(user.getEmail());

                dataMail.setSubject(Const.SEND_MAIL_SUBJECT.SUBJECT_CHANGEPASS);

                Map<String, Object> props = new HashMap<>();
                props.put("name", user.getFirstName() + user.getLastName());
                props.put("newPassword", rndPass);

                dataMail.setProps(props);

                mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CHANGEPASSWORD);
            }
            return ResponseEntity.status(200).body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }



}
