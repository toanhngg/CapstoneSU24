package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Transport;
import fpt.CapstoneSU24.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
public class TransportController {
    private final TransportRepository transportRepository;
    @Autowired
    public TransportController(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }
    @GetMapping("/getAllTransport")
    public ResponseEntity<?> getAllTransport() {
        List<Transport> transport = transportRepository.findAll();
        return ResponseEntity.ok(transport);
    }
}
