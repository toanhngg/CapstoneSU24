package fpt.CapstoneSU24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseCustomDTO {
    private int productId;
    private String productName;
    private String categoryName;
    private String avatarManufacturer;
    private String NameManufacturer;
    private int userId;

}
