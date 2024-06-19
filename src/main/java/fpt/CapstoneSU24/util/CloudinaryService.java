package fpt.CapstoneSU24.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private final   Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(@org.jetbrains.annotations.NotNull MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        return uploadResult.get("url").toString();
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

    public String uploadImageAndGetPublicId(@org.jetbrains.annotations.NotNull MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        String publicId = uploadResult.get("public_id").toString();
        return publicId;
    }

}
