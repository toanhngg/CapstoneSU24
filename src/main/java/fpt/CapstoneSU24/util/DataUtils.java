package fpt.CapstoneSU24.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataUtils {
    public static String generateTempPwd(int length) {
        String numbers = "012345678";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }

    public static Map<Integer, String> getComponentMapping() {
        Map<Integer, String> componentMap = new HashMap<>();
        try {

            InputStream inputStream = DataUtils.class.getClassLoader().getResourceAsStream("Json/reportComponents.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode componentsNode = rootNode.path("reportComponents");


            if (componentsNode.isArray()) {
                for (JsonNode componentNode : componentsNode) {
                    int code = componentNode.path("code").asInt();
                    String name = componentNode.path("name").asText();
                    componentMap.put(code, name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return componentMap;
    }
}
