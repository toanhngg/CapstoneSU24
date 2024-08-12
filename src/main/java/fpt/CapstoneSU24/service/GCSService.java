package fpt.CapstoneSU24.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GCSService {
    private final String bucketName = "storagetraceorigin";

    public Storage getStorage() throws IOException {
        String jsonCredentials = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"automated-rune-429819-u7\",\n" +
                "  \"private_key_id\": \"bee5b2f3653200446851e2a789b2d111e993ecf6\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC5bdcp9VM3Q0VS\\n8fT9Bi89YUizvnbEaEEBcoIDjFD7l+DUL5mks3bMZABotb0jnC7GcK1IBbRI8es0\\nfBNP5/066KA9VJ447cqB/1HnB3XifZDeTZhVhfOXJ1xesI3+h9Xd/OsQ4qljwgvW\\nRjh3Uyj9iMV86pApDk2FbSzHk2vQ8SHfFm2D5S2jKdJbaTHzThO9mYnYx84xMOEt\\ntyZiQUKf7sq7w96TmYQnVV9HP8eK5s2eX0kaVvTxPpbTbOgZBrigJq6Y0ZgaAfxx\\n28cfQKuB+3E/gjzM+s59PtHP25ZvxSTX3oRCoNO+0R2oF/9r7KjMhutKsrb7MaPd\\nXcU71qTDAgMBAAECggEACRxQegtHr2tquIyS0OvA3m8tN0egZj4JE50grjPytqnb\\nrhE4p1L5/0jajEqBpIJD+hQHL4a4OO1J9SYzfLWVR5Oua9UAgSON051L1OIU9K/e\\nsk11sp4GPeL9MtnR4DOkWmfW1NmOIQyjsD96/sHjPbG+okiUFLBTNr8KeDPSFE3v\\nPwmzh50ABlxbhVlxYVQKAThn9VF9FC6fg73PwUmX08Foq6KDRyrpejmwkYwoi+R2\\n+UO7ZrZ15ZnOhPDu6eRn7VbTUhIEEoyLpVI5UqdGwgS3ybHmaeOaP4j+D1KLzeON\\na4H6EvQQj1G72H7FOLCNFv0FoKxJIR7hRkTADdlrwQKBgQDmEgFeTo6cjDlpK4fE\\nkn4RwnMiZAengJA+CXnP/qlmVloZdN2Aue+7Fr91OwOoPpJE2RFsHSFgoCmTG5Uu\\nBE/WMx/Ve83QLKYhKd5PcWIV7YcU5uXtuLYtEqNOoJx0Y3XeyAN5DTQsN7MCYq06\\nXI2tQdnnp8qe2tXR19fntnPUeQKBgQDOU9gdahYJ/DIXAQ5eNhYGA9zZfESSXQOQ\\nMNrLK9V3XsoVr5dEl/qCYCeo6ne9+3mDi4de3J+/PlfJnxTL+6U/yGU+zlwEn2WK\\nW47oL5ImLDAplx/1GTIsxkFjW7hde1jedvCK+ck8Fq1HTDSSOJJb76LJ8PxNwjnr\\nsHPfOPIcGwKBgAQsjXPNjw7OCHic/pCVa84crgQQtobWcUd+2oPoheBCbs4Jbb9Z\\nID1Ps1eII4/Y2sR+/67rdGCRZ0+w72rvTxd+w/QWk+xT8wuk+9CLKRFmxs2dRsyi\\nTkPRE79ocI44A7pv/Igksi6fMBM6ARSO+08KY86tH6L0K27sMJGWoNFZAoGBAJU6\\nPwzHNblzFiddNC8fnz4qfQkvv8i8TV90dkqMIZB8llyo6xdJdH3nXcfmDUTzJSfn\\nOrl5iHChfozs6fW+w/4lylRqnFMbli1Dm1CDV4kUXKE4FH0JBCkuGwzpwaabF+OO\\n0tsxXkJ/hEQU8dDv6atk3rCAA3uaBppYvPWuEi4RAoGBAM+w8I9yxUZgOD/LUDvz\\nzKhFnxDCpSwVAaPLHXwPLplcBmJ9xxlDTY6TZPJkxHr7fUZhSy3w1H0tvrLtb25F\\nAeevNwaU/sMSkbDS+B0fNZcRml4JWBytXmQUfgXu2kyegltNdvG7lKH5cI63GLei\\nXxpgRc/z27TCEx/h2yH8BmWx\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"nguyenthanh@automated-rune-429819-u7.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"103845124309748674455\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/nguyenthanh%40automated-rune-429819-u7.iam.gserviceaccount.com\",\n" +
                "  \"universe_domain\": \"googleapis.com\"\n" +
                "}\n";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new ByteArrayInputStream(jsonCredentials.getBytes()));
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

    public String uploadFile(MultipartFile file, String name, int id) throws IOException {
        deleteFileIfExists("model3D", id + "");
        byte[] content = file.getBytes();
        Storage storage = getStorage();
        Blob blob = storage.create(Blob.newBuilder(bucketName, name).build(), content);
        return blob.getMediaLink();
    }
    public String uploadFile(MultipartFile file, String name) throws IOException {
        byte[] content = file.getBytes();
        Storage storage = getStorage();
        Blob blob = storage.create(Blob.newBuilder(bucketName, name).build(), content);
        return blob.getMediaLink();
    }
    public void deleteFileIfExists(String folderName, String baseFileName) throws IOException {
            Storage storage = getStorage();

        Iterable<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(folderName + "/")).iterateAll();

            for (Blob blob : blobs) {
                String filePath = blob.getName();
                String[] pathParts = filePath.split("/");
                if (pathParts.length > 1) {
                    String fileName = pathParts[pathParts.length - 1];
                    if (fileName.startsWith(baseFileName + ".")) {
                        boolean deleted = storage.delete(bucketName, filePath);
                    }
                }
            }
    }
    public String getFileLink(String fileName) throws IOException {
        Storage storage = getStorage();
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new RuntimeException("Bucket không tồn tại.");
        }

        Iterable<Blob> blobs = bucket.list(Storage.BlobListOption.prefix("model3D/")).iterateAll();

        // Tìm blob có đuôi .stl và trả về liên kết của nó
        for (Blob blob : blobs) {
            String blobName = blob.getName();
            // Kiểm tra nếu blobName bắt đầu với tiền tố và kết thúc với .stl
            if (blobName.contains("/"+fileName+".")) {
                return blob.getMediaLink();
            }
        }
        return "";
    }

}
