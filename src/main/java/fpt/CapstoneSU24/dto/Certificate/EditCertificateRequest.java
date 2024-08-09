package fpt.CapstoneSU24.dto.Certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class EditCertificateRequest {
    @NotNull(message = "certId is required")
    public Integer certId;
    @NotNull(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    public String name;
    @NotNull(message = "file is not null")
    public List<String> file;
    @NotNull(message = "Issuance date is required")
    public Long issuanceDate;
    @NotNull(message = "Issuance authority is required")
    @Size(max = 100, message = "Issuance authority must be less than 100 characters")
    public String issuanceAuthority;

    public Integer getCertId() {
        return certId;
    }

    public String getName() {
        return name;
    }

    public List<String> getFile() {
        return file;
    }

    public Long getIssuanceDate() {
        return issuanceDate;
    }

    public String getIssuanceAuthority() {
        return issuanceAuthority;
    }
}