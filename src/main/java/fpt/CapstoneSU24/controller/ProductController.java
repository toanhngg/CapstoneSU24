package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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
    CertificateRepository certificateRepository;
    @Autowired
    ImageProductRepository imageProductRepository;
    @Autowired
    ItemRepository itemRepository;
    @PostMapping("/addProduct")
    public ResponseEntity addProduct(@RequestBody String req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        JSONObject jsonReq = new JSONObject(req);
        if (currentUser.getRole().getRoleId() == 2) {
            //add product
            Product product = new Product();
            product.setProductName(jsonReq.getString("productName"));
            product.setCategory(categoryRepository.findOneByCategoryId(jsonReq.getInt("categoryId")));
            product.setUnitPrice(jsonReq.getString("unitPrice"));
            product.setDimensions(jsonReq.getString("dimensions"));
            product.setMaterial(jsonReq.getString("material"));
            product.setWeight(jsonReq.getFloat("weight"));
            product.setDescription(jsonReq.getString("description"));
            product.setWarranty(jsonReq.getInt("warranty"));
            product.setCreateAt(System.currentTimeMillis());
            product.setManufacturer(currentUser);
            product.setCertificate(certificateRepository.findOneByCertificateId(jsonReq.getInt("certificateId")));
            productRepository.save(product);
            //save image
            for (Object obj : (JSONArray)jsonReq.get("images")) {
                String element = (String) obj;
                byte[] bytes = element.getBytes();
                imageProductRepository.save(new ImageProduct(0, jsonReq.getString("productName"),bytes, product));
            }

//            return ResponseEntity.status(200).body(new String(bytes, StandardCharsets.UTF_8));
            return ResponseEntity.status(200).body("successfully");
        } else {
            throw new AccessDeniedException("");
        }
    }

    @GetMapping("/findAllProductByManufacturerId")
    public ResponseEntity findAllProductByManufacturerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
            List<Product> productList = productRepository.findAllByManufacturerId(currentUser.getUserId());
            return ResponseEntity.status(200).body(productList);
    }
    @PostMapping("/findImgByProductId")
    public ResponseEntity findImgByProductId(@RequestBody String req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        JSONObject jsonReq = new JSONObject(req);
        if (productRepository.findOneByProductId(jsonReq.getInt("productId")).getManufacturer().getUserId() == currentUser.getUserId()) {
            List<ImageProduct> imageProductList = imageProductRepository.findAllByProductId(jsonReq.getInt("productId"));
            List<String> listImg = new ArrayList<String>();
            for (ImageProduct i : imageProductList) {
                listImg.add(new String(i.getImage(), StandardCharsets.UTF_8));
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", listImg);
            return ResponseEntity.status(200).body(jsonObject.toString());
        }else{
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }
    @PostMapping("/deleteProductById")
    public ResponseEntity deleteProductById(@RequestBody String req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        JSONObject jsonReq = new JSONObject(req);
        int productId = jsonReq.getInt("productId");
        if (productRepository.findOneByProductId(productId).getManufacturer().getUserId() == currentUser.getUserId()) {
                if(itemRepository.findAllByProductId(productId).isEmpty()){
                    productRepository.deleteOneByProductId(productId);
                    return ResponseEntity.status(200).body("delete product success");
                }else {
                    return ResponseEntity.status(500).body("product can't delete because product have instants");
                }

        }else{
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }
}
