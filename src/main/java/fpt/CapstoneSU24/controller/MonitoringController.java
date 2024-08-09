package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.repository.ProductRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.ELKService;
import fpt.CapstoneSU24.service.MonitoringService;
import fpt.CapstoneSU24.service.MultipleThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {
    @Autowired
    MonitoringService monitoringService;
    @GetMapping("/admin")
    public ResponseEntity<?> getMonitoringByAdmin() throws IOException {
        return monitoringService.getMonitoringByAdmin();
    }
}