package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.JwtService;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity addCategory(@Valid @RequestBody CreateCategoryRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 2){
           Category category = new Category(0,req.getName(), req.getDescription(), userRepository.findOneByUserId(currentUser.getUserId()));
           categoryRepository.save(category);
           return ResponseEntity.status(200).body("successfully");
        }else {
            return ResponseEntity.status(500).body("Your account not permitted to handle this action");
        }
    }
    @PostMapping("/deleteById")
    public ResponseEntity deleteById(@Valid @RequestBody IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        try {
            Category category = categoryRepository.findOneByCategoryId(req.getId());
            if (category.getUser().getUserId() == currentUser.getUserId()) {
                    categoryRepository.deleteById(req.getId());
                return ResponseEntity.status(200).body("Successfully");
            }else{
                return ResponseEntity.status(500).body("Your account not permitted to handle this action");
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body("Can't find category by userId");
        }

    }
}
