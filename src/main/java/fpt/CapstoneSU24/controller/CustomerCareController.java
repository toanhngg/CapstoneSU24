package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.CustomerCareDTO;
import fpt.CapstoneSU24.model.CustomerCare;
import fpt.CapstoneSU24.service.CustomerCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CustomerCare>> getAllCustomerCare() {
        List<CustomerCare> customerCareList = customerCareService.getAllCustomerCare();
        return new ResponseEntity<>(customerCareList, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerCare>> searchCustomerCare(@RequestParam String keyword) {
        List<CustomerCare> customerCareList = customerCareService.searchCustomerCare(keyword);
        return new ResponseEntity<>(customerCareList, HttpStatus.OK);
    }
//    @PutMapping("/updateStatus")
//    public ResponseEntity<CustomerCare> updateStatus(@RequestBody UpdateStatusDTO updateStatusDTO) {
//        Optional<CustomerCare> updatedCustomerCare = customerCareService.updateStatus(updateStatusDTO.getCareId(), updateStatusDTO.getStatus());
//        return updatedCustomerCare.map(customerCare -> new ResponseEntity<>(customerCare, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
}
