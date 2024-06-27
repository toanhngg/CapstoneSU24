package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.ImageProduct;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.AddProductRequest;
import fpt.CapstoneSU24.payload.EditProductRequest;
import fpt.CapstoneSU24.payload.FilterSearchRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.service.ProductService;
import fpt.CapstoneSU24.util.CloudinaryService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@Valid @RequestBody AddProductRequest req) {
        return productService.addProduct(req);
    }

//    - có thêm trường product Id
//    - nếu không ko edit image (images, avatar) thì để là ""
    @PostMapping("/editProduct")
    public ResponseEntity<?> editProduct(@Valid @RequestBody EditProductRequest req) {
        return productService.editProduct(req);
    }


    @PostMapping("/findAllProductByManufacturerId")
    public ResponseEntity<?> findAllProductByManufacturerId(@Valid @RequestBody FilterSearchRequest req) {
        return productService.findAllProductByManufacturerId(req);

    }
    @PostMapping("/findImgByProductId")
    public ResponseEntity<?> findImgByProductId(@Valid @RequestBody IdRequest req) {
        return productService.findImgByProductId(req);

    }
    @PostMapping("/findProductById")
    public ResponseEntity<?> findProductById(@Valid @RequestBody IdRequest req) {
        return productService.findProductById(req);

    }
    @PostMapping("/deleteProductById")
    public ResponseEntity<?> deleteProductById(@Valid @RequestBody IdRequest req) {
        return productService.deleteProductById(req);

    }
}
