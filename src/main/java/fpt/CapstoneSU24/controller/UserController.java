package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/User")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/findAll")
    public ResponseEntity test() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}
