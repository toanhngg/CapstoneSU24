package fpt.CapstoneSU24.service;


import fpt.CapstoneSU24.dto.*;
import fpt.CapstoneSU24.dto.payload.*;
import fpt.CapstoneSU24.mapper.ProductMapper;
import fpt.CapstoneSU24.mapper.UserMapper;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageProductRepository imageProductRepository;
    private final ItemRepository itemRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final GCSService gcsService;
    private final LocationRepository locationRepository;
    private final PartyRepository partyRepository;
    private final ItemLogRepository itemLogRepository;
    private final EventTypeRepository eventTypeRepository;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


    @Autowired
    public ProductService(GCSService gcsService, ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             ImageProductRepository imageProductRepository, ItemRepository itemRepository,
                             CloudinaryService cloudinaryService,ProductMapper productMapper,
                          UserRepository userRepository, UserMapper userMapper,
                          LocationRepository locationRepository,
                          PartyRepository partyRepository,
                          ItemLogRepository itemLogRepository,
                          EventTypeRepository eventTypeRepository) {
        this.productRepository = productRepository;
        this.imageProductRepository = imageProductRepository;
        this.itemRepository = itemRepository;
        this.cloudinaryService = cloudinaryService;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.gcsService = gcsService;
        this.locationRepository = locationRepository;
        this.partyRepository = partyRepository;
        this.itemLogRepository = itemLogRepository;
        this.eventTypeRepository = eventTypeRepository;

    }

    public ResponseEntity addProduct(AddProductRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleId() == 2) {
            try {
                if (categoryRepository.findOneByCategoryId(req.getCategoryId()) == null)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("can not find id category");

                Product product = new Product();
                product.setProductName(req.getProductName());
                product.setCategory(categoryRepository.findOneByCategoryId(req.getCategoryId()));
                product.setDimensions(req.getDimensions());
                product.setMaterial(req.getMaterial());
                product.setWeight(req.getWeight());
                product.setDescription(req.getDescription());
                product.setWarranty(req.getWarranty());
                product.setCreateAt(System.currentTimeMillis());
                product.setManufacturer(currentUser);
                productRepository.save(product);
                //save image
                for (String obj : req.getImages()) {
                    String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj), "");
                    imageProductRepository.save(new ImageProduct(0, filePath, product));
                }
                //save ava
                String filePathAvatar = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(req.getAvatar()), "avatar/" + product.getProductId());
                imageProductRepository.save(new ImageProduct(0, filePathAvatar, product));
//                if (!req.getFile3D().isEmpty()) {
//                    String filePathFile3D = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToModel3DFile(req.getFile3D()), "file3d/" + product.getProductId());
//                    imageProductRepository.save(new ImageProduct(0, filePathFile3D, product));
//                }
                //            return ResponseEntity.status(200).body(new String(bytes, StandardCharsets.UTF_8));
                return ResponseEntity.status(HttpStatus.OK).body("add product successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error add new product");
            }
            //add product
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("your account is not allowed for this action");
        }
    }

    public ResponseEntity editProduct(EditProductRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        List<Item> items = itemRepository.findAllByProductId(req.getProductId());
        if(items.size() != 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("can't edit because the product have item");
        }
        if (currentUser.getRole().getRoleId() == 2) {
            try {
                Product product = productRepository.findOneByProductId(req.getProductId());
                if (product == null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("product id don't exists");
                }
                product.setProductName(req.getProductName());
                product.setCategory(categoryRepository.findOneByCategoryId(req.getCategoryId()));
//            product.setUnitPrice(req.getUnitPrice());
                product.setDimensions(req.getDimensions());
                product.setMaterial(req.getMaterial());
                product.setWeight(req.getWeight());
                product.setDescription(req.getDescription());
                product.setWarranty(req.getWarranty());
                product.setCreateAt(System.currentTimeMillis());
                productRepository.save(product);
                //save image
                if (!req.getImages().isEmpty()) {
                    try {
                        imageProductRepository.deleteImageProductWithFilePathNotStartingWithAvatar(product.getProductId());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                    for (String obj : req.getImages()) {
                        String filePath = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(obj), "");
                        imageProductRepository.save(new ImageProduct(0, filePath, product));
                    }
                }
                //save ava
                if (!req.getAvatar().isEmpty()) {
                    imageProductRepository.deleteImageProductWithFilePathStartingWithAvatar(product.getProductId());
                    String filePathAvatar = cloudinaryService.uploadImageAndGetPublicId(cloudinaryService.convertBase64ToImgFile(req.getAvatar()), "avatar/" + product.getProductId());
                    imageProductRepository.save(new ImageProduct(0, filePathAvatar, product));
                }
//                if (!req.getFile3D().isEmpty()) {
//                    String filePathFile3D = cloudinaryService.updateImage("file3d/" + product.getProductId(),cloudinaryService.convertBase64ToModel3DFile(req.getFile3D()));
//                    imageProductRepository.save(new ImageProduct(0, filePathFile3D, product));
//                }

                //            return ResponseEntity.status(200).body(new String(bytes, StandardCharsets.UTF_8));
                return ResponseEntity.status(HttpStatus.OK).body("edit product successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error edit product");
            }
            //add product
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("your account is not allowed for this action");
        }
    }

    public ResponseEntity findAllProductByManufacturerId(FilterSearchProductRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        try {
            Page<Product> products = null;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
            if (req.getStartDate() != 0 && req.getEndDate() != 0) {
//                if ((req.getName() != null && !req.getName().trim().isEmpty()) || (req.getCategoryName() != null && !req.getCategoryName().trim().isEmpty())) {
//                    products = productRepository.findAllProductWithDateAndKeyword(currentUser.getUserId(), "%" + req.getCategoryName() + "%", "%" + req.getName() + "%", req.getStartDate(), req.getEndDate(), pageable);
//                } else{
                products = productRepository.findAllProductWithDate(currentUser.getUserId(), "%" + req.getCategoryName() + "%", "%" + req.getName() + "%", req.getStartDate(), req.getEndDate(), pageable);

                // products = productRepository.findAllProduct(currentUser.getUserId(), "%"+req.getCategoryName()+"%", "%"+req.getName()+"%",pageable);
               // }
            } else {
                products = productRepository.findAllProduct(currentUser.getUserId(), "%"+req.getCategoryName()+"%", "%"+req.getName()+"%",pageable);
            }
            return ResponseEntity.status(HttpStatus.OK).body(products.map(productMapper::productToProductDTOResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when fetching data");
        }
    }
    public ResponseEntity ViewProductByManufacturerId(ViewProductRequest req) {
//        try {
//            Page<Product> products = null;
//            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createAt")) :
//                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createAt")) :
//                            PageRequest.of(req.getPageNumber(), req.getPageSize());
//            if (req.getStartDate() != 0 && req.getEndDate() != 0) {
//                products = productRepository.findAllProductWithDate(req.getId(), "%"+req.getCategoryName()+"%", "%"+req.getName()+"%", req.getStartDate(), req.getEndDate(), pageable);
//            } else {
//                products = productRepository.findAllProduct(req.getId(), "%"+req.getCategoryName()+"%", "%"+req.getName()+"%",pageable);
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(products.map(productMapper::productToViewProductDTOResponse));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when fetching data");
//        }
        try {
            List<Product> products = new ArrayList<>();
            if(req.getCategoryId() == 0)
            {
                products = productRepository.findAllProduct(req.getId());
            }else {
                products = productRepository.findAllProduct(req.getId(), req.getCategoryId());
            }
            List<ViewProductDTOResponse> productDTOs = products.stream()
                    .map(productMapper::productToViewProductDTOResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(productDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when fetching data");
        }
    }

    public ResponseEntity findImgByProductId(IdRequest req) {
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
            return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("your account is not allowed for this action");
        }
    }


    public ResponseEntity<?> findProductDetailById(IdRequest req) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Product p = productRepository.findOneByProductId(req.getId());
        if(p == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("product id isn't exist");
        }
        if (p.getManufacturer().getUserId() == currentUser.getUserId()) {
            return ResponseEntity.status(HttpStatus.OK).body(productMapper.productToProductDetailDTOResponse(p));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("your account is not allowed for this action");
        }
    }

    public ResponseEntity deleteProductById(IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Product productDelete = productRepository.findOneByProductId(req.getId());
        List<Item> items = itemRepository.findAllByProductId(req.getId());
        if(items.size() != 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("can't delete because the product have item");
        }
        if(productDelete != null){
            if (productDelete.getManufacturer().getUserId() == currentUser.getUserId()) {
                    imageProductRepository.deleteByProductId(req.getId());
                    productRepository.deleteOneByProductId(req.getId());
                    return ResponseEntity.status(HttpStatus.OK).body("delete product success");

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("your account is not allowed for this action");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not exist product id="+ req.getId());
    }
    public ResponseEntity getManufacturerByProductId(IdRequest req) {
        User user = userRepository.findUserByProductId(req.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.usersToUserViewDetailDTO(user));
    }
    public ResponseEntity countRegisteredProduct() {
       List<Product> products = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products.size());
    }
    public ResponseEntity findProductIdByName(ProductNameRequest req) {
       Product products = productRepository.findOneByProductName(req.getProductName());
        return ResponseEntity.status(HttpStatus.OK).body(products.getProductId());
    }

    public JSONObject infoProductForMonitor(long startDate, long endDate) {
        List<Product> monthlyProducts = productRepository.findAllProductByCreateAtBetween(startDate, endDate);
        List<Product> products = productRepository.findAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", products.size());
        jsonObject.put("monthly",monthlyProducts.size());
        return jsonObject;
    }
    public ResponseEntity<?> saveFileAI(MultipartFile weights, MultipartFile classNames, MultipartFile model, String description) throws IOException {
        log.info("uploadFileAISuccessful/"+description);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 1){

            gcsService.uploadFile(weights, weights.getOriginalFilename());
            gcsService.uploadFile(classNames, classNames.getOriginalFilename());
            gcsService.uploadFile(model, model.getOriginalFilename());

            return ResponseEntity.status(200).body("save file successfully");
        }
        return ResponseEntity.status(500).body("your account isn't permitted for this action");
    }
    public ResponseEntity<?> saveModel3D(MultipartFile file3D, int id) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole().getRoleId() == 2){
            String originalFilename = file3D.getOriginalFilename();

            // Kiểm tra và lấy đuôi file
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                int dotIndex = originalFilename.lastIndexOf(".");
                fileExtension = originalFilename.substring(dotIndex);
            }
            String newFileName = "model3D/"+ id + fileExtension;
           String path = gcsService.uploadFile(file3D, newFileName, id);
            return ResponseEntity.status(200).body("save model 3d successfully");
        }
        return ResponseEntity.status(500).body("your account isn't permitted for this action");
    }

    public ResponseEntity<?> requestScanImage(RequestScanImageDTO req) throws IOException {
        int status = 0;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Product product = productRepository.findOneByProductId(Integer.parseInt(req.getProductId()));
        product.setRequestScanDate(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli());

        List<String> imagePaths = req.getImage();
        for (String imagePath : imagePaths) {
            ImageProduct imageProduct = new ImageProduct();
            imageProduct.setFilePath(imagePath);
            imageProduct.setType(1);
            imageProduct.setProduct(product);
            product.getImageProducts().add(imageProduct);
        }

        productRepository.save(product);


        return ResponseEntity.ok("");
    }

    public ResponseEntity<?> approvalImageRequest(ChangeStatusImageProduct req) throws IOException {
        for (String id : req.getProductId()) {
            Product product = productRepository.findOneByProductId(Integer.parseInt(id));
            product.getImageProducts().forEach(img -> {
                if (img.getType() == 1) {
                    img.setType(3);
                    imageProductRepository.save(img);
                }
            });
        }
        return ResponseEntity.ok("Status updated successfully");
    }

    public ResponseEntity<?> getImageHadUpload(GetImageHasUploadDTO req) throws IOException {
        Product product = productRepository.findOneByProductId(Integer.parseInt(req.getProductId()));
        List<String> filePaths = product.getImageProducts().stream()
                .filter(imageProduct -> imageProduct.getType() == 3 || imageProduct.getType() == 1)
                .map(ImageProduct::getFilePath)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filePaths);
    }

    public ResponseEntity<?> getimageRequest(FilterListToScan req)
    {
        int status = 0;

        Page<Product> products = null;
        Pageable pageable = req.isDesc() ? PageRequest.of(req.getPage(), req.getSize(), Sort.by(Sort.Direction.ASC, req.getOrderBy())) :
                !req.isDesc() ? PageRequest.of(req.getPage(), req.getSize(), Sort.by(Sort.Direction.DESC, req.getOrderBy())) :
                        PageRequest.of(req.getPage(), req.getSize());
        products = productRepository.findProductRequestScanList(req.getProductName(), req.getManufactorName(), req.getProductId(), pageable);

        Page<ListImageToScanDTO> listImageToScanDTOS = products.map(product -> {
            ListImageToScanDTO listImageToScanDTO = new ListImageToScanDTO();
            listImageToScanDTO.setProductId(String.valueOf(product.getProductId()));
            listImageToScanDTO.setProductName(product.getProductName());
            listImageToScanDTO.setManufactorName(product.getManufacturer().getEmail());
            listImageToScanDTO.setRequestDate(product.getRequestScanDate());
            List<String> filePaths = product.getImageProducts().stream()
                    .filter(imageProduct -> imageProduct.getType() == 1)
                    .map(ImageProduct::getFilePath)
                    .collect(Collectors.toList());
            listImageToScanDTO.setFilePath(filePaths);
            return  listImageToScanDTO;
        });


        return ResponseEntity.ok(listImageToScanDTOS);
    }


    public ResponseEntity<?> getInfoByProductId(int productId) {
        ProductResponseCustomDTO productDetail = productRepository.findDetailProductAndUser(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productDetail);
    }

    public Boolean productForManu(int userId, int productId) {
        Product productDetail = productRepository.listProduct(userId,productId);
        return productDetail != null;
    }

    public ResponseEntity<String> disableProductById(IdRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole().getRoleId() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: insufficient permissions.");
        }

        Product product = productRepository.findOneByProductId(req.getId());

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID=" + req.getId());
        }

        List<Item> items = itemRepository.findAllByProductIdLock(req.getId());

        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No items found for the product ID=" + req.getId());
        }

        try {
            for (Item item : items) {
                if (item.getStatus() == 2) {
                    itemRepository.updateItemStatus(item.getProductRecognition(), 1); // Mở khóa
                }else{
                    itemRepository.updateItemStatus(item.getProductRecognition(), 2); // Tam khóa
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Product and related items disabled successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error disabling product: " + e.getMessage());
        }
    }

}
