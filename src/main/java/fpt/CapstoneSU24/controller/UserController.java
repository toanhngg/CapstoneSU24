package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.B03.B03_GetDataGridDTO;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender emailSender;

    @GetMapping("/getAllUser")
    public ResponseEntity getAllUser() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/getDataToTable")
    public ResponseEntity<List<B03_GetDataGridDTO>> getUsersByEmail(@RequestParam(value = "email", required = false) String email,
                                                                    @RequestParam(required = false) Integer roleId,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo,
                                                                    @RequestParam(required = false, defaultValue = "createOn") String orderBy,
                                                                    @RequestParam(required = false, defaultValue = "false") Boolean isAsc,
                                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                                    @RequestParam(required = false, defaultValue = "10") int size) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);

        Long timestampFrom = dateFrom != null ? dateFrom.getTime() / 1000 : null;
        Long timestampTo = dateTo != null ? dateTo.getTime() / 1000 : null;

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = userRepository.findByFilters(email, roleId, status, timestampFrom, timestampTo, pageable);

        List<B03_GetDataGridDTO> B03_GetDataGridDTOs = userPage.getContent().stream().map(user -> {
            B03_GetDataGridDTO B03_GetDataGridDTO = new B03_GetDataGridDTO();
            B03_GetDataGridDTO.setUserId(user.getUserId());
            B03_GetDataGridDTO.setEmail(user.getEmail());
            B03_GetDataGridDTO.setRoleId(user.getRole().getRoleId());
            B03_GetDataGridDTO.setRoleName(user.getRole().getRoleName());
            B03_GetDataGridDTO.setName(user.getFirstName() + " " + user.getLastName());
            B03_GetDataGridDTO.setDescription(user.getDescription());
            B03_GetDataGridDTO.setAddress(user.getAddress());
            B03_GetDataGridDTO.setCountry(user.getCountry());
            B03_GetDataGridDTO.setPhone(user.getPhone());
            B03_GetDataGridDTO.setDateOfBirth(new Date(user.getDateOfBirth() * 1000L));
            B03_GetDataGridDTO.setStatus(user.getStatus());
            B03_GetDataGridDTO.setCreateOn(new Date(user.getCreateOn() * 1000L));
            B03_GetDataGridDTO.setUsername(user.getUsername());
            return B03_GetDataGridDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(B03_GetDataGridDTOs);

    }

    @PutMapping("/lockUser")
    public ResponseEntity<String> updateStatus(@RequestParam int userId, @RequestParam int status) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(status);
            userRepository.save(user);
            return ResponseEntity.ok("update " + userId + " updated to " + status + ".");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        return "Email sent successfully to " + to;
    }

    @PutMapping("/updateUserDescriptions")
    public ResponseEntity<String> updateUserDescriptions(@RequestBody List<B03_GetDataGridDTO> userUpdateRequests) {
        for (B03_GetDataGridDTO updateRequest : userUpdateRequests) {
            Optional<User> optionalUser = userRepository.findById(updateRequest.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setDescription(updateRequest.getDescription());
                userRepository.save(user);
            } else {
                return ResponseEntity.badRequest().body("User with ID " + updateRequest.getUserId() + " not found.");
            }
        }
        return ResponseEntity.ok("User descriptions updated successfully.");
    }

}
