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
        return ResponseEntity.ok(categoryService.fillAllCategory());
    }

    @PostMapping("/addCategory")
    public ResponseEntity addCategory(@Valid @RequestBody CreateCategoryRequest req) {
        int status = categoryService.addCategory(req);
        if (status == 0) {
            return ResponseEntity.ok("create successfully");
        }
        return ResponseEntity.ok("Your account not permitted to handle this action");

    }

    @PostMapping("/editCategory")
    public ResponseEntity editCategory(@Valid @RequestBody EditCategoryRequest req) {
        int status = categoryService.editCategory(req);
        if (status == 0) {
            return ResponseEntity.ok("edit successfully");
        }
        return ResponseEntity.status(500).body(status == 1 ? "Can't find category by id" : "Your account not permitted to handle this action");
    }

    @PostMapping("/deleteById")
    public ResponseEntity deleteById(@Valid @RequestBody IdRequest req) {
        int status = categoryService.deleteCategory(req);
        if (status == 0) {
            return ResponseEntity.ok("delete successfully");
        }
        return ResponseEntity.status(500).body(status == 1 ? "can't find category by id" : status == 2 ? "error delete category" : "Your account not permitted to handle this action");
    }
}
