package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.payload.EditCategoryRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(categoryService.fillAllCategory());
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CreateCategoryRequest req) {
        int status = categoryService.addCategory(req);
        if (status == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("create successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Your account not permitted to handle this action");

    }

    @PostMapping("/editCategory")
    public ResponseEntity<?> editCategory(@Valid @RequestBody EditCategoryRequest req) {
        int status = categoryService.editCategory(req);
        if (status == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("edit successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status == 1 ? "Can't find category by id" : "Your account not permitted to handle this action");
    }

    @PostMapping("/deleteById")
    public ResponseEntity<?> deleteById(@Valid @RequestBody IdRequest req) {
        int status = categoryService.deleteCategory(req);
        if (status == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("delete successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status == 1 ? "can't find category by id" : status == 2 ? "error delete category" : "Your account not permitted to handle this action");
    }
}
