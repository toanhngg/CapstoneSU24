package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.RegisterUserDto;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.AuthenticationService;
import fpt.CapstoneSU24.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterUserDto registerUserDto) {
        if(userRepository.findOneByEmail(registerUserDto.getEmail()) == null){
            try {
                User registeredUser = authenticationService.signup(registerUserDto);
                return ResponseEntity.ok(registeredUser);
            }catch (Exception e){
                return ResponseEntity.ok().body(e);
            }
        }
        return  ResponseEntity.ok().body("your email already exists");
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody String req, HttpServletResponse response) {
        JSONObject jsonReq = new JSONObject(req);
        String email = jsonReq.getString("email");
        String password = jsonReq.getString("password");
        User authenticatedUser = authenticationService.authenticate(email, password);
        String jwtToken = jwtService.generateToken(authenticatedUser);
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

}
