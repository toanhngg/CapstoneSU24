package fpt.CapstoneSU24.dto.Certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreateCertificateRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    public String name;
    public String file;
    @NotNull(message = "Issuance date is required")
    public LocalDateTime issuanceDate;
    @NotBlank(message = "Issuance authority is required")
    @Size(max = 100, message = "Issuance authority must be less than 100 characters")
    public String issuanceAuthority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public LocalDateTime getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(LocalDateTime issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getIssuanceAuthority() {
        return issuanceAuthority;
    }

    public void setIssuanceAuthority(String issuanceAuthority) {
        this.issuanceAuthority = issuanceAuthority;
    }

}
