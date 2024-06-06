package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ChangePasswordDto;
import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.RegisterRequest;
import fpt.CapstoneSU24.repository.AuthTokenRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.AuthenticationService;
import fpt.CapstoneSU24.service.JwtService;
import fpt.CapstoneSU24.payload.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
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
                .secure(true).httpOnly(true)
                .path("/")
                .sameSite("None")
                .domain(null)
                .maxAge(-1)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("jwt: " + jwtToken);
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
                            .secure(true).httpOnly(true)
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
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            if (userRepository.findOneByEmail(changePasswordDto.getEmail()) == null) {
                return ResponseEntity.status(500).body("Your email does not exist");
            } else {
                try {
                    User user = authenticationService.authenticate(changePasswordDto.getEmail(), changePasswordDto.getOldPassword());
                    user.setPassword(changePasswordDto.getPassword());
                    authenticationService.ChangePassword(user);
                    return ResponseEntity.status(200).body("Change password successfully");
                } catch (BadCredentialsException e) {
                    return ResponseEntity.status(401).body("Invalid Password");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }


}
