package fpt.CapstoneSU24.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OriginDTO {
    private String productName;
    private String productRecognition;
    private int productId;
    private int orgNameId;

    //  private String dimensions;
    //private float weight;
    //private String material;
    private String orgName;
    private String phone;
    private String fullName;
    private long createAt;
    private String categoryName;
    private LocationDTO locationDTO;
    private String descriptionProduct;
    private String descriptionOrigin;
    private int warranty;
    private Boolean checkPoint;

    // private double CoordinateX;
   // private double CoordinateY;
  //  private String AddressOrigin;
    private List<String> image;
    private String model3D;

}
