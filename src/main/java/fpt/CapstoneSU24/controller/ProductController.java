package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.payload.AddProductRequest;
import fpt.CapstoneSU24.payload.EditProductRequest;
import fpt.CapstoneSU24.payload.FilterSearchRequest;
import fpt.CapstoneSU24.payload.IdRequest;
import fpt.CapstoneSU24.repository.*;
import fpt.CapstoneSU24.util.CloudinaryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    CloudinaryService cloudinaryService;
    @PostMapping("/addProduct")
    public ResponseEntity addProduct(@Valid @RequestBody AddProductRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 2){
            try {
                Product product = new Product();
                product.setProductName(req.getProductName());
                product.setCategory(categoryRepository.findOneByCategoryId(req.getCategoryId()));
//            product.setUnitPrice(req.getUnitPrice());
                product.setDimensions(req.getDimensions());
                product.setMaterial(req.getMaterial());
                product.setWeight(req.getWeight());
                product.setDescription(req.getDescription());
                product.setWarranty(req.getWarranty());
                product.setCreateAt(System.currentTimeMillis());
                product.setManufacturer(currentUser);
//                product.setCertificate(certificateRepository.findOneByCertificateId(req.getCertificateId()));
                productRepository.save(product);
                //save image
                for (String obj : req.getImages()) {
                    String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj),"");
                    imageProductRepository.save(new ImageProduct(0,filePath, product));
                }
                //save ava
                String filePathAvatar = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(req.getAvatar()), "avatar/"+product.getProductId());
                imageProductRepository.save(new ImageProduct(0,filePathAvatar, product));
                //            return ResponseEntity.status(200).body(new String(bytes, StandardCharsets.UTF_8));
                return ResponseEntity.status(200).body("successfully");
            }catch (Exception e){
                return ResponseEntity.status(500).body("error add new product");
            }
            //add product
        }else {
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }


//    - có thêm trường product Id
//    - nếu không ko edit image (images, avatar) thì để là ""
    @PostMapping("/editProduct")
    public ResponseEntity editProduct(@Valid @RequestBody EditProductRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 2){
            try {
                Product product = productRepository.findOneByProductId(req.getProductId());
                product.setProductName(req.getProductName());
                product.setCategory(categoryRepository.findOneByCategoryId(req.getCategoryId()));
//            product.setUnitPrice(req.getUnitPrice());
                product.setDimensions(req.getDimensions());
                product.setMaterial(req.getMaterial());
                product.setWeight(req.getWeight());
                product.setDescription(req.getDescription());
                product.setWarranty(req.getWarranty());
                product.setCreateAt(System.currentTimeMillis());
                product.setManufacturer(currentUser);
//                product.setCertificate(certificateRepository.findOneByCertificateId(req.getCertificateId()));
                productRepository.save(product);
                //save image
                if(!req.getImages().isEmpty()){
                    for (String obj : req.getImages()) {
                        String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj),"");
                        imageProductRepository.save(new ImageProduct(0,filePath, product));
                    }
                }
                //save ava
                if(!req.getAvatar().isEmpty()){
                    String filePathAvatar = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(req.getAvatar()), "avatar/"+product.getProductId());
                    imageProductRepository.save(new ImageProduct(0,filePathAvatar, product));
                }

                //            return ResponseEntity.status(200).body(new String(bytes, StandardCharsets.UTF_8));
                return ResponseEntity.status(200).body("successfully");
            }catch (Exception e){
                return ResponseEntity.status(500).body("error edit new product");
            }
            //add product
        }else {
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }


    @PostMapping("/findAllProductByManufacturerId")
    public ResponseEntity findAllProductByManufacturerId(@Valid @RequestBody FilterSearchRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        try {
            Page<Product> products = null;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
            if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                products = productRepository.findByManufacturerAndCreateAtBetween(currentUser,req.getStartDate(), req.getEndDate(), pageable);
            } else {
                products = productRepository.findByManufacturerAndProductNameContaining(currentUser, req.getName(), pageable);
            }
            return ResponseEntity.status(200).body(products);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error when fetching data");
        }
    }
    @PostMapping("/findImgByProductId")
    public ResponseEntity findImgByProductId(@Valid @RequestBody IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (productRepository.findOneByProductId(req.getId()).getManufacturer().getUserId() == currentUser.getUserId()) {
            List<ImageProduct> imageProductList = imageProductRepository.findAllByProductId(req.getId());
            List<String> listImg = new ArrayList<String>();
            for (ImageProduct i : imageProductList) {
                listImg.add(i.getFilePath());
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", listImg);
            return ResponseEntity.status(200).body(jsonObject.toString());
        }else{
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }
    @PostMapping("/findProductById")
    public ResponseEntity findProductById(@Valid @RequestBody IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (productRepository.findOneByProductId(req.getId()).getManufacturer().getUserId() == currentUser.getUserId()) {
            Product product = productRepository.findOneByProductId(req.getId());
            return ResponseEntity.status(200).body(product);
        }else{
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }
    @PostMapping("/deleteProductById")
    public ResponseEntity deleteProductById(@Valid @RequestBody IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (productRepository.findOneByProductId(req.getId()).getManufacturer().getUserId() == currentUser.getUserId()) {
                if(itemRepository.findAllByProductId(req.getId()).isEmpty()){
                    productRepository.deleteOneByProductId(req.getId());
                    return ResponseEntity.status(200).body("delete product success");
                }else {
                    return ResponseEntity.status(500).body("product can't delete because product have instants");
                }

        }else{
            return ResponseEntity.status(404).body("your account is not allowed for this action");
        }
    }
}
