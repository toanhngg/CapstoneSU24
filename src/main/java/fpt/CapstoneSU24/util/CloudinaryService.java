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

}
