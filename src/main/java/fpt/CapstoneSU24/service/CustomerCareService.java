package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CustomerCareDTO;
import fpt.CapstoneSU24.dto.SearchCustomerCareDTO;
import fpt.CapstoneSU24.mapper.CustomerCareMapper;
import fpt.CapstoneSU24.model.CustomerCare;
import fpt.CapstoneSU24.repository.CustomerCareRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerCareService {
    private final CustomerCareRepository customerCareRepository;
    private final CustomerCareMapper customerCareMapper;
    private final UserRepository userRepository;

    @Autowired
    public CustomerCareService(CustomerCareRepository customerCareRepository,CustomerCareMapper customerCareMapper,
                               UserRepository userRepository) {
        this.customerCareRepository = customerCareRepository;
        this.customerCareMapper = customerCareMapper;
        this.userRepository = userRepository;
    }

    public CustomerCare addCustomerCare(CustomerCareDTO customerCareDTO) {
        CustomerCare customerCare = new CustomerCare();
        customerCare.setCustomerName(customerCareDTO.getCustomerName());
        customerCare.setCustomerEmail(customerCareDTO.getCustomerEmail());
        customerCare.setCustomerPhone(customerCareDTO.getCustomerPhone());
        customerCare.setContent(customerCareDTO.getContent());
        customerCare.setTimestamp(System.currentTimeMillis());
        customerCare.setStatus(customerCareDTO.getStatus());

        return customerCareRepository.save(customerCare);
    }

    public List<CustomerCare> getAllCustomerCare() {
        return customerCareRepository.findAll();
    }

    public ResponseEntity<?> searchCustomerCare(SearchCustomerCareDTO req) {
        Pageable pageable = req.getType().equals("desc") ?
                PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "timestamp")) :
                PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "timestamp"));

        Page<CustomerCare> customerCarePage;
        try {
            if (req.getStatus() == 3) {
                if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                    customerCarePage = customerCareRepository.searchCustomerCareWithDate(req.getKeyword(), req.getStartDate(), req.getEndDate(), pageable);
                } else {
                    customerCarePage = customerCareRepository.searchCustomerCare(req.getKeyword(), pageable);
                }
            }
            else {
                if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                    customerCarePage = customerCareRepository.searchCustomerCareWithDateAndStatus(req.getKeyword(), req.getStartDate(), req.getEndDate(), req.getStatus(), pageable);
                } else {
                    customerCarePage = customerCareRepository.searchCustomerCareWithStatus(req.getKeyword(), req.getStatus(), pageable);
                }

            }
            return ResponseEntity.status(HttpStatus.OK).body(customerCarePage.map(customerCareMapper::customerCareToCustomerCareDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when fetching data");
        }
    }

    public Optional<CustomerCare> updateStatus(int careId, int status, String note,int userId) {
        try {
            Optional<CustomerCare> optionalCustomerCare = customerCareRepository.findById(careId);
            if (optionalCustomerCare.isPresent()) {
                CustomerCare customerCare = optionalCustomerCare.get();
                customerCare.setStatus(status);
                customerCare.setNote(note);
                customerCare.setUserCheck(userRepository.findOneByUserId(userId));
                customerCareRepository.save(customerCare);
                return Optional.of(customerCare);
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            System.err.println("Error updating status: " + ex.getMessage());
            return Optional.empty();
        }
    }

}
