package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CategoryForManagerDTO;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CategoryServiceTest_FindAllCategory {
        @Mock
        private CategoryRepository categoryRepository;

        @Mock
        private ProductRepository productRepository;

        @InjectMocks
        private CategoryService categoryService;

        private Category category1;
        private Category category2;
        private Category category3;

        @BeforeEach
        public void setUp() {
            category1 = new Category();
            category1.setCategoryId(1);
            category1.setName("Category 1");

            category2 = new Category();
            category2.setCategoryId(2);
            category2.setName("Category 2");

            category3 = new Category();
            category3.setCategoryId(3);
            category3.setName("Category 3");
        }

        @Test
        public void testFindAllManager_WithCategoriesAndProducts() {
            when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2, category3));
            when(productRepository.countProductsByCategoryId(1)).thenReturn(5);
            when(productRepository.countProductsByCategoryId(2)).thenReturn(0);
            when(productRepository.countProductsByCategoryId(3)).thenReturn(10);

            ResponseEntity<?> response = categoryService.findAllManager();

            List<CategoryForManagerDTO> categoryList = (List<CategoryForManagerDTO>) response.getBody();
            assertEquals(3, categoryList.size());
            assertEquals(1, categoryList.get(0).getStatus());
            assertEquals(0, categoryList.get(1).getStatus());
            assertEquals(1, categoryList.get(2).getStatus());
        }

        @Test
        public void testFindAllManager_WithoutCategories() {
            when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

            ResponseEntity<?> response = categoryService.findAllManager();

            List<CategoryForManagerDTO> categoryList = (List<CategoryForManagerDTO>) response.getBody();
            assertEquals(0, categoryList.size());
        }

        @Test
        public void testFindAllManager_WithCategoriesWithoutProducts() {
            when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
            when(productRepository.countProductsByCategoryId(1)).thenReturn(0);
            when(productRepository.countProductsByCategoryId(2)).thenReturn(0);

            ResponseEntity<?> response = categoryService.findAllManager();

            List<CategoryForManagerDTO> categoryList = (List<CategoryForManagerDTO>) response.getBody();
            assertEquals(2, categoryList.size());
            assertEquals(0, categoryList.get(0).getStatus());
            assertEquals(0, categoryList.get(1).getStatus());
        }
}
