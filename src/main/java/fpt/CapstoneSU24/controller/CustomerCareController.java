package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.CustomerCareDTO;
import fpt.CapstoneSU24.dto.SearchCustomerCareDTO;
import fpt.CapstoneSU24.dto.UpdateStatusDTO;
import fpt.CapstoneSU24.model.CustomerCare;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.service.CustomerCareService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CustomerCare> addCustomerCare(@Valid @RequestBody CustomerCareDTO customerCareDTO) {
        CustomerCare newCustomerCare = customerCareService.addCustomerCare(customerCareDTO);
        return new ResponseEntity<>(newCustomerCare, HttpStatus.CREATED);
    }
    @PostMapping("/addByUser")
    public ResponseEntity<CustomerCare> addByUser(@Valid @RequestBody CustomerCareDTO customerCareDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        customerCareDTO.setCustomerEmail(currentUser.getEmail());
        customerCareDTO.setCustomerName(currentUser.getLastName() +  " " + currentUser.getFirstName());
        customerCareDTO.setCustomerPhone(currentUser.getPhone());
        CustomerCare newCustomerCare = customerCareService.addCustomerCare(customerCareDTO);
        return new ResponseEntity<>(newCustomerCare, HttpStatus.CREATED);
    }
//    @GetMapping("/getAll")
//    public ResponseEntity<?> getAllCustomerCare() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        if (currentUser.getRole().getRoleId() == 1) {
//            Page<Item> items;
//            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")) :
//                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt")) :
//                            PageRequest.of(req.getPageNumber(), req.getPageSize());
//            List<CustomerCare> customerCareList = customerCareService.getAllCustomerCare();
//            return new ResponseEntity<>(customerCareList, HttpStatus.OK);
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//        }
//    }

    //    @GetMapping("/search")
//    public ResponseEntity<?> searchCustomerCare(@Valid @RequestBody SearchCustomerCareDTO req) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//         User currentUser = (User) authentication.getPrincipal();
//            if (currentUser.getRole().getRoleId() == 1) {
//                Page<Item> items;
//                Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")) :
//                        req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt")) :
//                                PageRequest.of(req.getPageNumber(), req.getPageSize());
//                List<CustomerCare> customerCareList = customerCareService.searchCustomerCare(req.getKeyword(), req.getStartDate(), req.getEndDate(),req.getStatus());
//                return new ResponseEntity<>(customerCareList, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
//            }
//
//    }
    @PostMapping ("/searchCustomerCare")
    public ResponseEntity<?> searchCustomerCare(@Valid @RequestBody SearchCustomerCareDTO req) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//
//        if (currentUser.getRole().getRoleId() == 1) {
            return customerCareService.searchCustomerCare(req);
//        } else {
//            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
//        }
    }
    @PostMapping ("/searchCustomerCareByUser")
    public ResponseEntity<?> searchCustomerCareByUser(@Valid @RequestBody SearchCustomerCareDTO req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        req.setKeyword(currentUser.getEmail());
//        if (currentUser.getRole().getRoleId() == 1) {
        return customerCareService.searchCustomerCare(req);
//        } else {
//            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
//        }
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody UpdateStatusDTO updateStatusDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() != 2) {
            Optional<CustomerCare> updatedCustomerCare = customerCareService.updateStatus(updateStatusDTO.getCareId(),
                    updateStatusDTO.getStatus(), updateStatusDTO.getNote(),currentUser.getUserId());
            return updatedCustomerCare.map(customerCare -> new ResponseEntity<>(customerCare, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
    @GetMapping("/countStatus")
    public ResponseEntity<?> countStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() != 2) {
            JSONObject countStatus = customerCareService.countStatus();
            return ResponseEntity.status(HttpStatus.OK).body(countStatus.toString());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
}
