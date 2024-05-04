package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.repository.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    private IMemberRepository iMemberRepository;
    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok(iMemberRepository.findAll());
    }


}
