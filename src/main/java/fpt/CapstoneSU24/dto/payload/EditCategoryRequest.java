package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotBlank;

public class EditCategoryRequest {
    @NotBlank(message = "The categoryId is required")
    private Integer categoryId;
    @NotBlank(message = "The name is required")
    private String name;
    @NotBlank(message = "The description is required")
    private String description;

    public Integer getCategoryId() {
        return categoryId;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
