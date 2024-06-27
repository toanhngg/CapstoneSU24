package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.payload.EditCategoryRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")

public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository,UserRepository userRepository,JwtService jwtService){
    this.categoryRepository = categoryRepository;
    this.userRepository = userRepository;
    this.jwtService = jwtService;
}

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return ResponseEntity.ok(categoryList);
    }
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CreateCategoryRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){
           Category category = new Category(0,req.getName(), req.getDescription());
           categoryRepository.save(category);
           return ResponseEntity.status(200).body("successfully");
        }else {
            return ResponseEntity.status(500).body("Your account not permitted to handle this action");
        }
    }
    @PostMapping("/editCategory")
    public ResponseEntity editCategory(@Valid @RequestBody EditCategoryRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){
            Category category = categoryRepository.findOneByCategoryId(req.getCategoryId());
            if(category == null) return ResponseEntity.status(500).body("Can't find category by id");
            category.setName(req.getName());
            category.setDescription(req.getDescription());
            category.setName(req.getName());
            categoryRepository.save(category);
            return ResponseEntity.status(200).body("successfully");
        }else {
            return ResponseEntity.status(500).body("Your account not permitted to handle this action");
        }
    }
    @PostMapping("/deleteById")
    public ResponseEntity<?> deleteById(@Valid @RequestBody IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
        try {
            Category category = categoryRepository.findOneByCategoryId(req.getId());
            if(category == null) return ResponseEntity.status(500).body("Can't find category by id");
            categoryRepository.deleteById(req.getId());
            return ResponseEntity.status(200).body("Successfully");
        }catch (Exception e){
            return ResponseEntity.status(500).body("error delete category");
        }
        }else{
            return ResponseEntity.status(500).body("Your account not permitted to handle this action");
        }
    }
}
