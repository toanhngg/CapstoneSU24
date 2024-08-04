package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.payload.*;
import fpt.CapstoneSU24.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<?> ViewProductByManufacturerId(@Valid @RequestBody ViewProductRequest req) {
        return productService.ViewProductByManufacturerId(req);
    }
    @PostMapping("/findImgByProductId")
    public ResponseEntity<?> findImgByProductId(@Valid @RequestBody IdRequest req) {
        return productService.findImgByProductId(req);
    }
    @PostMapping("/findProductDetailById")
    public ResponseEntity<?> findProductDetailById(@Valid @RequestBody IdRequest req) throws IOException {
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
    @PostMapping("/findProductIdByName")
    public ResponseEntity<?> findProductIdByName(@Valid @RequestBody ProductNameRequest req) {
        return productService.findProductIdByName(req);
    }
    @PostMapping("/saveFileAI")
    public ResponseEntity<?> saveFileAI(@RequestParam("weights") MultipartFile weights, @RequestParam("classNames") MultipartFile classNames, @RequestParam("model") MultipartFile model, @RequestParam("description") String description) throws IOException {
        return productService.saveFileAI(weights, classNames, model, description);
    }
    @PostMapping("/saveModel3D/{id}")
    public ResponseEntity<?> saveModel3D(@RequestParam("file3D") MultipartFile file3D, @PathVariable("id") int id) throws IOException {
        return productService.saveModel3D(file3D, id);
    }
}
