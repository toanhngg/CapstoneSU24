package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.payload.EditCategoryRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.CategoryService;
import fpt.CapstoneSU24.service.JwtService;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private JwtService jwtService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        return categoryService.findAll();
    }

    @PostMapping("/addCategory")
    public ResponseEntity addCategory(@Valid @RequestBody CreateCategoryRequest req) {
        return categoryService.addCategory(req);
    }

    @PostMapping("/editCategory")
    public ResponseEntity editCategory(@Valid @RequestBody EditCategoryRequest req) {
        return categoryService.editCategory(req);
    }

    @PostMapping("/deleteById")
    public ResponseEntity deleteById(@Valid @RequestBody IdRequest req) {
        return categoryService.deleteById(req);
    }
}
