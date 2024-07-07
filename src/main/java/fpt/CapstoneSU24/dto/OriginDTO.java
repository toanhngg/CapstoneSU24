package fpt.CapstoneSU24.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OriginDTO {
    private String ProductName;
    private String ProductRecognition;
    private String OrgName;
    private String Phone;
    private String FullName;
    private long CreateAt;
    private String DescriptionProduct;
    private String DescriptionOrigin;
    private int Warranty;
    private double CoordinateX;
    private double CoordinateY;
    private String AddressOrigin;
    private List<String> Image;
}
