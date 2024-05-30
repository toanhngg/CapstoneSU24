package fpt.CapstoneSU24.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class QRCodeGenerator {

    public String generateProductCode(String productName, String category) {
        // Lấy UUID ngẫu nhiên
        UUID uuid = UUID.randomUUID();

        // Lấy thời gian hiện tại dưới dạng milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Tạo chuỗi mã sản phẩm dựa trên các thông tin
        String rawCode = productName + "-" + category + "-" + uuid.toString() + "-" + currentTimeMillis;

        // Mã hóa chuỗi thành Base64 để dễ đọc hơn
        String encodedCode = Base64.getUrlEncoder().encodeToString(rawCode.getBytes(StandardCharsets.UTF_8));

        return encodedCode;
    }

}