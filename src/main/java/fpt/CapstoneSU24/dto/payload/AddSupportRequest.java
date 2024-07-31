package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AddSupportRequest {
    @NotBlank(message = "The name is required")
    private String title;
    @NotBlank(message = "The content is required")
    private String content;
    @NotNull(message = "The images is required")
    private List<String> images;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getImages() {
        return images;
    }
}
