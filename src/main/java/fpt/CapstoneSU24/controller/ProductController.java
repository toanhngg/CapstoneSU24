package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.payload.*;
import fpt.CapstoneSU24.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> findAllProductByManufacturerId(@Valid @RequestBody FilterSearchProductRequest req) {
        return productService.findAllProductByManufacturerId(req);
    }
    //tìm kiếm toàn bộ product theo id của manufacturer (không cần auth)
    @PostMapping("/ViewProductByManufacturerId")
    public ResponseEntity<?> ViewProductByManufacturerId(@Valid @RequestBody FilterSearchProductByIdRequest req) {
        return productService.ViewProductByManufacturerId(req);
    }
    @PostMapping("/findImgByProductId")
    public ResponseEntity<?> findImgByProductId(@Valid @RequestBody IdRequest req) {
        return productService.findImgByProductId(req);
    }
    @PostMapping("/findProductDetailById")
    public ResponseEntity<?> findProductDetailById(@Valid @RequestBody IdRequest req) {
        return productService.findProductDetailById(req);
    }
    @PostMapping("/deleteProductById")
    public ResponseEntity<?> deleteProductById(@Valid @RequestBody IdRequest req) {
        return productService.deleteProductById(req);
    }
    @PostMapping("/getManufacturerByProductId")
    public ResponseEntity<?> getManufacturerByProductId(@Valid @RequestBody IdRequest req) {
        return productService.getManufacturerByProductId(req);
    }
    @GetMapping("/countRegisteredProduct")
    public ResponseEntity<?> countRegisteredProduct() {
        return productService.countRegisteredProduct();
    }
}
