package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.payload.EditCategoryRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final  CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> fillAllCategory(){
        return categoryRepository.findAll();
    }
    public int addCategory(CreateCategoryRequest req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){
            Category category = new Category(0,req.getName(), req.getDescription());
            categoryRepository.save(category);
            return 0;
        }else {
            return 1;
        }
    }
    public int editCategory(EditCategoryRequest req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){
            Category category = categoryRepository.findOneByCategoryId(req.getCategoryId());
            if(category == null) return 1;
            category.setName(req.getName());
            category.setDescription(req.getDescription());
            category.setName(req.getName());
            categoryRepository.save(category);
            return 0;
        }else {
            return 2;
        }
    }
    public int deleteCategory(IdRequest req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 1) {
            try {
                Category category = categoryRepository.findOneByCategoryId(req.getId());
                if (category == null) return 1;
                categoryRepository.deleteById(req.getId());
                return 0;
            } catch (Exception e) {
                return 2;
            }
        } else {
            return  3;
        }
    }
}