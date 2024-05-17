package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.JwtRequest;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.Crypto;
import fpt.CapstoneSU24.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/User")
public class UserController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    Crypto crypto;
//    @Autowired
//    private User user;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/findAll")
    public ResponseEntity test() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) {
        User user = new User();
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            String a = crypto.encrypt(password);
            user = userRepository.findOneByEmailAndPassword(username, crypto.encrypt(password));
            if (username == null) {
                return ResponseEntity.ok("NOT FOUND");
            } else {
                try {
                    authenticate(username, password);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e.toString());
        }
        final String token = jwtTokenUtil.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token) // key & value
                .secure(true).httpOnly(true)
                .path("/")
                .sameSite("None")
                .domain(null)
                .maxAge(-1)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("jwt: " + token);
        return ResponseEntity.ok().body(user);
    }
    private void authenticate(String username, String password) throws Exception {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
