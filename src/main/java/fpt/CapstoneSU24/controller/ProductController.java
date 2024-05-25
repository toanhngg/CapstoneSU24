package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OriginRepository originRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/addProduct")
    public ResponseEntity addProduct(@RequestBody String req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        JSONObject jsonReq = new JSONObject(req);
        if(currentUser.getRole().getRoleId() == 2){
            Origin origin = new Origin(0, userRepository.findOneByUserId(currentUser.getUserId()), System.currentTimeMillis(), "", "");
            originRepository.save(origin);
            String productName = jsonReq.getString("productName");
            User manufacturer = userRepository.findOneByUserId(currentUser.getUserId());
            Category category = categoryRepository.findOneByCategoryId(jsonReq.getInt("categoryId"));
            String unitPrice = jsonReq.getString("unitPrice");
            String dimensions = jsonReq.getString("dimensions");
            String material = jsonReq.getString("material");
            String supportingDocuments = jsonReq.getString("supportingDocuments");
            Product product = new Product(0, productName, manufacturer, category, origin, unitPrice, dimensions, material, supportingDocuments, System.currentTimeMillis(),10, 5,null);
            productRepository.save(product);
            return ResponseEntity.status(200).body("successfully");
        }else {
           throw new AccessDeniedException("");
        }
    }
    @GetMapping("/findAllProductByManufacturerId")
    public ResponseEntity findAllProductByManufacturerId(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 2){
          List<Product> productList = productRepository.findAllByManufacturerId(currentUser.getUserId());
            return ResponseEntity.status(200).body(productList);
        }else {
            throw new AccessDeniedException("");
        }
    }
}
