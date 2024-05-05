package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Member;
import fpt.CapstoneSU24.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @GetMapping("/test")
    public ResponseEntity test() {
        List<Member> memberList = memberRepository.findAll();
        return ResponseEntity.ok(memberList);
    }
}
