package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CategoryForManagerDTO;
import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.dto.payload.*;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final  CategoryRepository categoryRepository;

    private  UserService userService;


    private final  ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return ResponseEntity.ok(categoryList);
    }

    public ResponseEntity<?> findAllManager() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryForManagerDTO> categoryList = categories.stream()
                .map(category -> {
                    int status = productRepository.countProductsByCategoryId(category.getCategoryId()) > 0 ? 1 : 0;
                    return new CategoryForManagerDTO(String.valueOf(category.getCategoryId()), category.getName(), status);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryList);
    }

    public ResponseEntity<?> getLastID() {
        long lastCateId = categoryRepository.findMaxId() + 1;
        return ResponseEntity.ok(lastCateId);
    }

    public ResponseEntity<?> addListCategory(List<CategoryForManagerDTO> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();


        if (currentUser.getRole().getRoleId() == 1) {

            List<Category> categoriesToDelete = categoryRepository.findCategoriesWithoutProducts();
            categoryRepository.deleteAll(categoriesToDelete);
            for (CategoryForManagerDTO req : request) {
                if (req.getStatus() == 0) {
                    Category existingCategory = categoryRepository.findOneByName(req.getName());
                    if (existingCategory == null) {
                        Category newCategory = new Category();
                        newCategory.setName(req.getName());
                        newCategory.setDescription("");
                        categoryRepository.save(newCategory);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("Categories processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account is not permitted to handle this action");
        }
    }



    public ResponseEntity addCategory(CreateCategoryRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){
            if(categoryRepository.findOneByName(req.getName()) == null) {
                Category category = new Category(0, req.getName(), req.getDescription(), null);
                categoryRepository.save(category);
                return ResponseEntity.status(HttpStatus.OK).body("successfully");
            }
            return ResponseEntity.status(HttpStatus.OK).body("your category name already exists");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Your account not permitted to handle this action");
        }
    }
    public ResponseEntity editCategory(EditCategoryRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){
            Category category = categoryRepository.findOneByCategoryId(req.getCategoryId());
            if(category == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find category by id");
            category.setName(req.getName());
            category.setDescription(req.getDescription());
            category.setName(req.getName());
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK).body("successfully");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Your account not permitted to handle this action");
        }
    }
    public ResponseEntity deleteById(IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            try {
                Category category = categoryRepository.findOneByCategoryId(req.getId());
                if(category == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find category by id");
                categoryRepository.deleteById(req.getId());
                return ResponseEntity.status(HttpStatus.OK).body("Successfully");
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error delete category");
            }
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Your account not permitted to handle this action");
        }
    }
    public ResponseEntity<?> search(FilterSearchForCategoryRequest req) {
        try {
            Page<Category> categories;
            Pageable pageable = PageRequest.of(req.getPageNumber(), req.getPageSize());
                categories = categoryRepository.findAllByNameContaining(req.getName(), pageable);
            return ResponseEntity.status(200).body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }
    public ResponseEntity<?> findCategoryByManufacturer(IdRequest req) {
        try {
            List<Category> categories = categoryRepository.findCategoryByManufacturer(req.getId());
            return ResponseEntity.status(200).body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }
}