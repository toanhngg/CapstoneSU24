package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
@Service
public class MonitoringService {
    private final MultipleThreadService multipleThreadService;
@Autowired
ELKService elkService;
    @Autowired
    public MonitoringService(MultipleThreadService multipleThreadService){
        this.multipleThreadService = multipleThreadService;
    }

   public ResponseEntity<?> getMonitoringByAdmin() throws IOException {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       User currentUser = (User) authentication.getPrincipal();
       if (currentUser.getRole().getRoleId() == 1) return ResponseEntity.status(200).body(multipleThreadService.getQueryMultipleThreadForDatabase().toString());
       return ResponseEntity.status(500).body("your account don't have permit for this action");
   }
}
