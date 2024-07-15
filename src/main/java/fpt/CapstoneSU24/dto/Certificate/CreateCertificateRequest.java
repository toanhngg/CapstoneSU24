package fpt.CapstoneSU24.dto.Certificate;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreateCertificateRequest {
    @NotBlank(message = "Name is required")
    public String name;
    @NotNull(message = "file is not null")
    public List<String> images;
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "Issuance date is required")
    public long issuanceDate;
    @NotBlank(message = "Issuance authority is required")
    public String issuanceAuthority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public long getIssuanceDate() {
        return issuanceDate;
    }

    public String getIssuanceAuthority() {
        return issuanceAuthority;
    }

    public void setIssuanceAuthority(String issuanceAuthority) {
        this.issuanceAuthority = issuanceAuthority;
    }

}