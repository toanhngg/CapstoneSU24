package fpt.CapstoneSU24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryByUserDTO {
    private int categoryId;
    private String name;
}
