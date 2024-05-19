package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    JwtTokenUtil jwtTokenUtil;

//    @GetMapping("/findAllProductByManufacturerId")
//    public ResponseEntity findAll(HttpServletRequest request) {
//        List<Product> productList = new ArrayList<>();
//        final String requestTokenHeader = request.getHeader("Cookie");
//        String email = null;
//        String jwtToken = null;
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("jwt=")) {
//            jwtToken = requestTokenHeader.substring(4);
//            try {
//                int roleId = jwtTokenUtil.getRoleIdFromToken(jwtToken);
//                int userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
//                if (roleId == 2) {
//                     productList = productRepository.findAllByManufacturerId(userId);
//                }
//            } catch (Exception e) {
//                return ResponseEntity.notFound().build();
//            }
//        }
//        return ResponseEntity.ok(productList);
//    }
//    @PostMapping("/AddProduct")
//    public ResponseEntity AddProduct(@RequestBody String req) {
//        JSONObject jsonReq = new JSONObject(req);
//        String productName = jsonReq.getString("productName");
////        User manufacturer = userRepository.findOneByUserId(jsonReq.getInt("manufacturerId"));
//        Origin origin = new Origin(0,userRepository.findOneByUserId(jsonReq.getInt("manufacturerId")),System.currentTimeMillis(),"","");
//        Category category = categoryRepository.findOneByCategoryId(jsonReq.getInt("categoryId"));
//        User currentOwner = null;
//        String unitPrice = jsonReq.getString("unitPrice");
//        long createdAt = jsonReq.getLong("createdAt");
//        String dimensions = jsonReq.getString("dimensions");
//        String material =jsonReq.getString("material");
//        String supportingDocuments = jsonReq.getString("supportingDocuments");
//        String productRecognition = jsonReq.getString("productRecognition");
//        Product product = new Product(0, productName, category, origin, currentOwner, unitPrice, createdAt, dimensions,material, supportingDocuments, productRecognition);
//        productRepository.save(product);
//            return ResponseEntity.ok("ok");
//    }
}
