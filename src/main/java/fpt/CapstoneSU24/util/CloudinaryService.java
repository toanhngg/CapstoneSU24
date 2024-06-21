package fpt.CapstoneSU24.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(@org.jetbrains.annotations.NotNull MultipartFile file, String customKey) throws IOException {
        /*
        ---------------In: File, customkey để rỗ hoặc null nếu không cần customkey)
        */
        if (customKey == null || customKey.isEmpty())
        {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("url").toString();
        }else{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", customKey
                    ));
            return uploadResult.get("url").toString();
        }
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    public byte[] downloadImage(String publicId) throws IOException {
        String url = cloudinary.url().generate(publicId);
        URL imageUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        InputStream inputStream = connection.getInputStream();
        return inputStream.readAllBytes();
    }

    public String uploadImageAndGetPublicId(@org.jetbrains.annotations.NotNull MultipartFile file, String customKey) throws IOException {
        String publicId;
        Map uploadResult;
        if (customKey.isEmpty() || customKey == null)
        {
             uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            publicId = uploadResult.get("public_id").toString();
            return publicId;
        }else {
             uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", customKey
                    ));
            publicId = uploadResult.get("public_id").toString();
            return publicId;
        }
    }
    public MultipartFile convertBase64ToImgFile(String base64String) throws IOException {
        try {
            return convertBase64ToMultipartFile(base64String, "image.png");
        } catch (IOException e) {
            System.err.println("Failed to create MultipartFile: " + e.getMessage());
            return null;
        }
    }
        public static MultipartFile convertBase64ToMultipartFile(String base64String, String fileName) throws IOException {
            // Decode Base64 string to byte array
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);

            // Create MultipartFile
            return new MockMultipartFile(
                    fileName,          // Filename
                    fileName,          // Original filename
                    "image/png",       // Content type
                    new ByteArrayInputStream(decodedBytes)  // InputStream
            );
        }
}
