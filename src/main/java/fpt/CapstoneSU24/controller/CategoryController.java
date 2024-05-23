package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @GetMapping("/findAll")
    public ResponseEntity findAll(HttpServletRequest request) {
            final String requestTokenHeader = request.getHeader("Cookie");
            String username = null;
            String jwtToken = null;
            if (requestTokenHeader != null && requestTokenHeader.startsWith("jwt=")) {
                jwtToken = requestTokenHeader.substring(4);
            }
        System.out.println(jwtService.extractUsername(jwtToken)+"hihi");
        List<Category> categoryList = categoryRepository.findAll();
        return ResponseEntity.ok(categoryList);
    }
    @PostMapping("/addCategory")
    public ResponseEntity addCategory(@RequestBody String req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        JSONObject jsonReq = new JSONObject(req);
        if(currentUser.getRole().getRoleId() == 2){
           Category category = new Category(0,jsonReq.getString("name"), jsonReq.getString("description"), userRepository.findOneByUserId(currentUser.getUserId()));
           categoryRepository.save(category);
           return ResponseEntity.status(200).body("successfully");
        }else {
            throw new AccessDeniedException("");
        }
    }
}
