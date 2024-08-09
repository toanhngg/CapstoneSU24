package fpt.CapstoneSU24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTOResponse {
    private int productId;
//    private String productName;
//    private String dimensions;
//    private String material;
//    private String description;
//    private long createAt;
//    private float weight;
//    private int warranty;
//    private String avatar;
    private List<String> listImages;
}
