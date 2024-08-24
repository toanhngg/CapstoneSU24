package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.ProductDTOResponse;
import fpt.CapstoneSU24.dto.ProductDetailDTOResponse;
import fpt.CapstoneSU24.dto.ProductImageDTOResponse;
import fpt.CapstoneSU24.dto.ViewProductDTOResponse;
import fpt.CapstoneSU24.model.Category;
import fpt.CapstoneSU24.model.ImageProduct;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.repository.CategoryRepository;
import fpt.CapstoneSU24.repository.ImageProductRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.service.CloudinaryService;
import fpt.CapstoneSU24.service.GCSService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ProductMapper {

    @Autowired
    private ImageProductRepository imageProductRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private GCSService gcsService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "avatar", ignore = true) // Ignore avatar for now, we'll set it manually
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "checkStatusDisable", ignore = true)
    public abstract ProductDTOResponse productToProductDTOResponse(Product product);
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "dimensions", target = "dimensions")
    @Mapping(source = "material", target = "material")
    @Mapping(source = "weight", target = "weight")
    @Mapping(target = "avatar", ignore = true) // Ignore avatar for now, we'll set it manually
    @Mapping(target = "checkStatusDisable", ignore = true)
    public abstract ViewProductDTOResponse productToViewProductDTOResponse(Product product);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "dimensions", target = "dimensions")
    @Mapping(source = "material", target = "material")
    @Mapping(source = "weight", target = "weight")
    @Mapping(target = "avatar", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract List<ViewProductDTOResponse> productToViewProductDTOResponse(List<Product> product);
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "dimensions", target = "dimensions")
    @Mapping(source = "material", target = "material")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createAt", target = "createAt")
    @Mapping(source = "weight", target = "weight")
    @Mapping(source = "warranty", target = "warranty")
    @Mapping(target = "model3D", ignore = true) // Ignore avatar for now, we'll set it manually
    @Mapping(target = "avatar", ignore = true) // Ignore avatar for now, we'll set it manually
    @Mapping(target = "listImages", ignore = true) // Ignore avatar for now, we'll set it manually

    public abstract ProductDetailDTOResponse productToProductDetailDTOResponse(Product product) throws IOException;



    @Mapping(source = "productId", target = "productId")
    @Mapping(target = "listImages", ignore = true) // Ignore avatar for now, we'll set it manually
    public abstract ProductImageDTOResponse productToProductImageDTOResponse(Product product);

    @AfterMapping
    protected void setAvatar(Product product, @MappingTarget ViewProductDTOResponse productDTO) {
        ImageProduct imageProduct = imageProductRepository.findAllByFilePath("avatar/"+product.getProductId());
        if (imageProduct != null) {
            productDTO.setAvatar(cloudinaryService.getImageUrl(imageProduct.getFilePath()));
        }
    }
    @AfterMapping
    protected void setStatus(Product product, @MappingTarget ProductDTOResponse productDTO) {
        // Tìm tất cả các Item liên kết với productId
        List<Item> items = itemRepository.findAllByProductId(productDTO.getProductId());

        if (items.isEmpty()) {
            productDTO.setCheckStatusDisable(0); // chưa khóa
        } else {
            boolean allItemsInStatusTwo = items.stream().allMatch(item -> item.getStatus() == 2);

            if (allItemsInStatusTwo) {
                productDTO.setCheckStatusDisable(1); // đã khóa
            } else {
                productDTO.setCheckStatusDisable(0); // chưa khóa
            }
        }
    }
    @AfterMapping
    protected void setStatus(Product product, @MappingTarget ViewProductDTOResponse productDTO) {
        // Tìm tất cả các Item liên kết với productId
        List<Item> items = itemRepository.findAllByProductId(productDTO.getProductId());

        if (items.isEmpty()) {
            productDTO.setCheckStatusDisable(0); // chưa khóa
        } else {
            boolean allItemsInStatusTwo = items.stream().allMatch(item -> item.getStatus() == 2);

            if (allItemsInStatusTwo) {
                productDTO.setCheckStatusDisable(1); // đã khóa
            } else {
                productDTO.setCheckStatusDisable(0); // chưa khóa
            }
        }
    }

    @AfterMapping
    protected void setAvatar(Product product, @MappingTarget ProductDTOResponse productDTO) {
        ImageProduct imageProduct = imageProductRepository.findAllByFilePath("avatar/"+product.getProductId());
        if (imageProduct != null) {
            productDTO.setAvatar(cloudinaryService.getImageUrl(imageProduct.getFilePath()));
        }
        List<Item> items = itemRepository.findAllByProductId(product.getProductId());
        if(items.size() == 0){
            productDTO.setStatus(0);
        }else{
            productDTO.setStatus(1);
        }
    }
    @AfterMapping
    protected void setAfter(Product product, @MappingTarget ProductDetailDTOResponse productDTO) throws IOException {

        //set avatar
        ImageProduct imageProduct = imageProductRepository.findAllByFilePath("avatar/"+product.getProductId());
        if (imageProduct != null) {
            productDTO.setAvatar(cloudinaryService.getImageUrl(imageProduct.getFilePath()));
        }

        //set model 3d
        productDTO.setModel3D(gcsService.getFileLink(String.valueOf(product.getProductId())));

        //set images
        List<String> imageProducts = imageProductRepository.findAllFilePathNotStartingWithAvatar(product.getProductId()).stream().map(filePath -> cloudinaryService.getImageUrl(filePath)).collect(Collectors.toList());
        productDTO.setListImages(imageProducts);

        //set category
        productDTO.setCategoryId(product.getCategory().getCategoryId());
        productDTO.setCategoryName(product.getCategory().getName());
    }

    @AfterMapping
    protected void setListImagesItem(Product product, @MappingTarget ProductImageDTOResponse productDTO) {
        List<String> imageProducts = imageProductRepository.findAllFilePathNotStartingWithAvatar(
                product.getProductId()).stream().map(filePath -> cloudinaryService.getImageUrl(filePath)).collect(Collectors.toList());
        productDTO.setListImages(imageProducts);
    }
//    @AfterMapping
//    protected void setListItemsView(Product product, @MappingTarget ProductDetailDTOResponse productDTO) {
//        List<Item> items = itemRepository.findAllByProductId(product.getProductId());
//        if (items != null) {
//            productDTO.setListItemsView(itemMapper.itemToItemViewDTOResponse(items));
//        }
//    }
}