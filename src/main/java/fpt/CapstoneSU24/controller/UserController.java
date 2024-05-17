package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.*;
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

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }
    @GetMapping("/getAllUser")
    public ResponseEntity getAllUser() {
      List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }


    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody String req, HttpServletResponse response) {
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

        return ResponseEntity.ok(authenticatedUser);
    }

}
