package fpt.CapstoneSU24.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class memberController {
    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("ookok");
    }
}
