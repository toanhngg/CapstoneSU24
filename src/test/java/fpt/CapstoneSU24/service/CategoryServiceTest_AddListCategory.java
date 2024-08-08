package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CategoryForManagerDTO;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest_AddListCategory {
    @InjectMocks
    private CategoryService yourService; // Replace with the actual service class name

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the security context and authentication
        User.UserBuilder userBuilder = User.withUsername("admin").password("password").roles("ADMIN");
        User user = (User) userBuilder.build();
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testAddListCategory_AdminUser_Success() {
        // Arrange
        List<CategoryForManagerDTO> request = Arrays.asList(
                new CategoryForManagerDTO("1","Category1", 0),
                new CategoryForManagerDTO("2","Category2", 0)
        );
        when(categoryRepository.findCategoriesWithoutProducts()).thenReturn(Arrays.asList(new Category()));
        when(categoryRepository.findOneByName("Category1")).thenReturn(null);
        when(categoryRepository.findOneByName("Category2")).thenReturn(null);

        // Act
        ResponseEntity<?> response = yourService.addListCategory(request);

        // Assert
        verify(categoryRepository).findCategoriesWithoutProducts();
        verify(categoryRepository).deleteAll(anyList());
        verify(categoryRepository).findOneByName("Category1");
        verify(categoryRepository).save(any(Category.class));
        verify(categoryRepository).findOneByName("Category2");
        verify(categoryRepository).save(any(Category.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Categories processed successfully", response.getBody());
    }

    @Test
    public void testAddListCategory_NonAdminUser_Forbidden() {
        // Arrange
        User.UserBuilder userBuilder = User.withUsername("user").password("password").roles("USER");
        User user = (User) userBuilder.build();
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<CategoryForManagerDTO> request = Arrays.asList(new CategoryForManagerDTO("1","Category1", 0));

        // Act
        ResponseEntity<?> response = yourService.addListCategory(request);

        // Assert
        verify(categoryRepository, never()).findCategoriesWithoutProducts();
        verify(categoryRepository, never()).deleteAll(anyList());
        verify(categoryRepository, never()).findOneByName(anyString());
        verify(categoryRepository, never()).save(any(Category.class));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Your account is not permitted to handle this action", response.getBody());
    }
}
