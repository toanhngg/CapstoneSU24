package fpt.CapstoneSU24.dto;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.web3j.codegen.SolidityFunctionWrapper;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GenerateJavaFromSolidity {
        public static void main(String[] args) {
            try {
                // Đường dẫn đến tệp JSON chứa ABI và BIN
                String jsonFilePath = "src/main/resources/Json/SimpleStorage.json";
                String outputDir = "src/main/java";
                String packageName = "fpt.CapstoneSU24.dto";

                // Đọc tệp JSON để lấy ABI và BIN
                JsonObject jsonObject = readJsonFile(jsonFilePath);
                String abi = jsonObject.get("abi").getAsString();
                String bin = jsonObject.get("bin").getAsString();

                // Tạo đối tượng Web3jCodegen
                SolidityFunctionWrapper codegen = new SolidityFunctionWrapper(true, true, true, 20);

                // Sinh mã Java từ ABI và BIN
                codegen.generateJavaFiles(
                        abi,
                        bin,
                        Collections.emptyList(), // Danh sách ABI, nếu không có có thể để rỗng
                        outputDir,
                        packageName,
                        Collections.emptyMap() // Có thể cung cấp cấu hình thêm nếu cần
                );

                System.out.println("Java code generation complete.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static JsonObject readJsonFile(String filePath) throws IOException {
            try (FileReader reader = new FileReader(new File(filePath))) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                return jsonElement.getAsJsonObject();
            }
        }
    }