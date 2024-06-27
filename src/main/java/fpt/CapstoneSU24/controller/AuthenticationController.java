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
import fpt.CapstoneSU24.util.JwtTokenUtil;
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
    private AuthenticationService authenticationService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            return ResponseEntity.status(200).body(authenticationService.signup(registerRequest) == 0 ? "create successfully" : "your email already exists");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("error create new account");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User authenticatedUser = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String jwtToken = jwtService.generateToken(authenticatedUser, authenticatedUser);

        response.setHeader(HttpHeaders.SET_COOKIE, jwtTokenUtil.setResponseCookie(jwtToken).toString());
        System.out.println("jwt: " + jwtToken);
        log.info("User {} is attempting to log in.", loginRequest.getEmail());
        return ResponseEntity.status(200).body("login successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        int n = authenticationService.logout(currentUser);
        if (n == 0) {
            response.setHeader(HttpHeaders.SET_COOKIE, jwtTokenUtil.setResponseCookie(null).toString());
            return ResponseEntity.status(500).body("logout successfully");
        } else
            return ResponseEntity.status(500).body(n == 1 ? "current don't have any account to logout" : "user don't have authToken");
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
        int status = authenticationService.forgotPassword(req);
        if (status == 0) {
            return ResponseEntity.status(200).body("successfully");
        } else {
            return ResponseEntity.status(500).body(status == 1? "email incorrect": "error forgot password");
        }
    }
}
