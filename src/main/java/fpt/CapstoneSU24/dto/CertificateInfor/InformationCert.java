package fpt.CapstoneSU24.dto.CertificateInfor;

import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformationCert {
    private Map<String, Object> props;

    private String orgName;
    private String itemName;
    private String supportingDocuments;
    private String createAt;
    private String productRecognition;

}
