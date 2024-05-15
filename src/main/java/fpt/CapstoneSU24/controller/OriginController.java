package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.repository.OriginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Origin")
public class OriginController {
    @Autowired
    private OriginRepository originRepository;
    @GetMapping("/findAll")
    public ResponseEntity test() {
        List<Origin> originList = originRepository.findAll();
        return ResponseEntity.ok(originList);
    }
}
