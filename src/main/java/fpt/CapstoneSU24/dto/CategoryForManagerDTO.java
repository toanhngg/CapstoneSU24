package fpt.CapstoneSU24.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryForManagerDTO {
    private String cateId;
    @NotBlank(message = "The name is required")
    private String name;
    private int status;

    public CategoryForManagerDTO(String cateId, String name, int status) {
        this.cateId = cateId;
        this.name = name;
        this.status = status;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
