package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReplySupportRequest {
    @Digits(integer = 5, fraction = 0, message = "Invalid digit format")
    @NotNull(message = "The id is required")
    @Min(value = 1, message = "The id must be a positive number")
    private Integer id;
    @NotBlank(message = "The content is required")
    private String content;
    @NotNull(message = "The images is required")
    private List<String> images;


    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<String> getImages() {
        return images;
    }
}
