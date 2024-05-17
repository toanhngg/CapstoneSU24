package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @PostMapping("/AddProduct")
    public ResponseEntity AddProduct(@RequestBody String req) {
        JSONObject jsonReq = new JSONObject(req);
        String productName = jsonReq.getString("productName");
        User manufacturer = userRepository.findOneByUserId(jsonReq.getInt("manufacturerId"));
        Category category = categoryRepository.findOneByCategoryId(jsonReq.getInt("categoryId"));
        User currentOwner = null;
        String unitPrice = jsonReq.getString("unitPrice");
        String createdAt = jsonReq.getString("createdAt");
        String dimensions = jsonReq.getString("dimensions");
        String material =jsonReq.getString("material");
        String supportingDocuments = jsonReq.getString("supportingDocuments");
        String productRecognition = jsonReq.getString("productRecognition");
        Product product = new Product(0, productName, manufacturer, category, currentOwner, unitPrice, createdAt, dimensions,material, supportingDocuments, productRecognition);
        productRepository.save(product);
            return ResponseEntity.ok("ok");
    }
}
