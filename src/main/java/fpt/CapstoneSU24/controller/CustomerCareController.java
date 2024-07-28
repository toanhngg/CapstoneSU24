package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.CustomerCareDTO;
import fpt.CapstoneSU24.dto.UpdateStatusDTO;
import fpt.CapstoneSU24.model.CustomerCare;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.service.CustomerCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/customercare")
@RestController
public class CustomerCareController {
    private final CustomerCareService customerCareService;
    @Autowired
    public  CustomerCareController(CustomerCareService customerCareService){
        this.customerCareService = customerCareService;
    }
    @PostMapping("/add")
    public ResponseEntity<CustomerCare> addCustomerCare(@RequestBody CustomerCareDTO customerCareDTO) {
        CustomerCare newCustomerCare = customerCareService.addCustomerCare(customerCareDTO);
        return new ResponseEntity<>(newCustomerCare, HttpStatus.CREATED);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomerCare() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            List<CustomerCare> customerCareList = customerCareService.getAllCustomerCare();
            return new ResponseEntity<>(customerCareList, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCustomerCare(
            @RequestParam String keyword,
            @RequestParam long startDate,
            @RequestParam long endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
         User currentUser = (User) authentication.getPrincipal();
            if (currentUser.getRole().getRoleId() == 1) {
                List<CustomerCare> customerCareList = customerCareService.searchCustomerCare(keyword, startDate, endDate);
                return new ResponseEntity<>(customerCareList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
            }

    }
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody UpdateStatusDTO updateStatusDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole().getRoleId() == 1) {
            Optional<CustomerCare> updatedCustomerCare = customerCareService.updateStatus(updateStatusDTO.getCareId(),
                    updateStatusDTO.getStatus(), updateStatusDTO.getNote());
            return updatedCustomerCare.map(customerCare -> new ResponseEntity<>(customerCare, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
}
