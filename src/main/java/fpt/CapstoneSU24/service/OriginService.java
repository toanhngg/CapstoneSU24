package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.OrgNameUserDTO;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OriginService {

    private final OriginRepository originRepository;
    private final UserRepository userRepository;


    @Autowired
    public OriginService(OriginRepository originRepository,UserRepository userRepository) {
        this.originRepository = originRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> findAll() {
        List<Origin> originList = originRepository.findAll();
        return ResponseEntity.ok(originList);
    }


//        Pageable topFive = PageRequest.of(0, 5);
//        List<Origin> originList = originRepository.findTop5OrgNames(topFive);
//        if (originList.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.OK).body(new OrgNameUserDTO(Collections.emptyList()));
//
//        } else {
//            List<OrgNameUserDTO> orgNameUserDTOS = originList.stream()
//                    .map(this::convertToItemLogDTO)
//                    .collect(Collectors.toList());
//            return ResponseEntity.status(HttpStatus.OK).body(orgNameUserDTOS);
//        }

//    private OrgNameUserDTO convertToItemLogDTO(Origin origin) {
//        OrgNameUserDTO dto = new OrgNameUserDTO();
//        dto.setOrgName(origin.getOrg_name());
//        User user = userRepository.findAllUserByOrgName(origin.getOrg_name());
//        dto.setUserId(user.getUserId());
//        dto.setUserImage(user.getProfileImage());
//        return dto;
//    }
}
