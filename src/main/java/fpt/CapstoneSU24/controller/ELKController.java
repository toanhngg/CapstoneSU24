package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.payload.*;
import fpt.CapstoneSU24.service.ELKService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/elk")

public class ELKController {
    @Autowired
    ELKService elkService;
    @PostMapping("/getNumberVisitsDiagram")
    public ResponseEntity<?> getNumberVisitsDiagram(@Valid @RequestBody SelectedTimeRequest req) throws IOException {
        return elkService.getNumberVisitsDiagram(req);
    }
    @GetMapping("/getNumberVisitsAllTime")
    public ResponseEntity<?> getNumberVisitsAllTime() throws IOException {
        return elkService.getNumberVisitsAllTime();
    }
//    @GetMapping("/getNumberTraceAllTime")
//    public ResponseEntity<?> getNumberTraceAllTime() throws IOException {
//        return elkService.getNumberTraceAllTime();
//    }
    @GetMapping("/getNumberTraceDiagram")
    public ResponseEntity<?> getNumberTraceDiagram() throws IOException {
        return elkService.getNumberTraceDiagram();
    }
    @GetMapping("/getHistoryUploadAI")
    public ResponseEntity<?> getHistoryUploadAI() throws IOException {
        return elkService.getHistoryUploadAI();
    }
}
