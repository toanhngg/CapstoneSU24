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
    public ResponseEntity findAll() {
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
    @PostMapping("/deleteById")
    public ResponseEntity deleteById(@RequestBody String req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        JSONObject jsonReq = new JSONObject(req);
        if(currentUser.getRole().getRoleId() == 2){
         try {
             categoryRepository.deleteById(jsonReq.getInt("idCategory"));
         }catch (Exception e){
             return ResponseEntity.ok(e);
         }
            return ResponseEntity.status(200).body("successfully");
        }else{
            return ResponseEntity.status(200).body("your account not permitted");
        }
    }
}
