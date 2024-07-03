package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.dto.payload.EditCategoryRequest;
import fpt.CapstoneSU24.dto.payload.FilterSearchRequest;
import fpt.CapstoneSU24.dto.payload.IdRequest;
import fpt.CapstoneSU24.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> search(@Valid @RequestBody FilterSearchRequest req) {
        return categoryService.searchItem(req);
    }

}
