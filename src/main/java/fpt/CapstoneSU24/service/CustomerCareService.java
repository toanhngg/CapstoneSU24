package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CustomerCareDTO;
import fpt.CapstoneSU24.model.CustomerCare;
import fpt.CapstoneSU24.repository.CustomerCareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCareService {
    private final CustomerCareRepository customerCareRepository;
    @Autowired
    public  CustomerCareService(CustomerCareRepository customerCareRepository){
        this.customerCareRepository = customerCareRepository;
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

    public List<CustomerCare> searchCustomerCare(String keyword) {
        return customerCareRepository.searchCustomerCare(keyword);
    }
}
