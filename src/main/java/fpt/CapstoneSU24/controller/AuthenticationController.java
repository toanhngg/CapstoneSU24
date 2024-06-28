package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ChangePasswordDto;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.ForgotPasswordRequest;
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
import org.springframework.http.HttpStatus;
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

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody RegisterRequest registerRequest) {
   return authenticationService.signup(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authenticationService.login(loginRequest, response);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        return authenticationService.logout(response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
     return authenticationService.changePassword(changePasswordDto);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        return authenticationService.forgetPassword(req);
    }
}
