package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.CategoryForManagerDTO;
import fpt.CapstoneSU24.dto.payload.*;
import fpt.CapstoneSU24.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")

public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/getCategoryForAdmin")
    public ResponseEntity<?> findAllToManager() {
        return categoryService.findAllManager();
    }

    @GetMapping("/getNextId")
    public ResponseEntity<?> getNextId() {
        return categoryService.getLastID();
    }


    @PostMapping("/addListCategory")
    public ResponseEntity<?> addListCategory(@Valid @RequestBody List<CategoryForManagerDTO> request) {
        return categoryService.addListCategory(request);
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CreateCategoryRequest req) {
        return categoryService.addCategory(req);
    }

    @PostMapping("/editCategory")
    public ResponseEntity<?> editCategory(@Valid @RequestBody EditCategoryRequest req) {
        return categoryService.editCategory(req);
    }

    @PostMapping("/deleteById")
    public ResponseEntity<?> deleteById(@Valid @RequestBody IdRequest req) {
        return categoryService.deleteById(req);
    }
    @PostMapping("/search")
    public ResponseEntity search(@Valid @RequestBody FilterSearchForCategoryRequest req) {
        return categoryService.search(req);
    }
    @PostMapping("/findCategoryByManufacturer")
    public ResponseEntity findCategoryByManufacturer(@Valid @RequestBody IdRequest req) {
        return categoryService.findCategoryByManufacturer(req);
    }
    @GetMapping("/findCategoryAuthentication")
    public ResponseEntity findCategoryAuthentication() {
        return categoryService.findCategoryAuthentication();
    }
}
