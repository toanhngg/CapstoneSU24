package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.B03.B03_GetDataGridDTO;
import fpt.CapstoneSU24.dto.B03.B03_RequestDTO;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.UserProfileDTO;
import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.Role;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.LoginRequest;
import fpt.CapstoneSU24.payload.VerifyAddressRequest;
import fpt.CapstoneSU24.payload.VerifyDDAddressRequest;
import fpt.CapstoneSU24.repository.AuthTokenRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.AuthenticationService;
import fpt.CapstoneSU24.service.EmailService;
import fpt.CapstoneSU24.service.JwtService;
import fpt.CapstoneSU24.util.Const;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Value("${key.open.cage.data}")
    private String apiKey;
    @Value("${url.open.cage.data}")
    private String urlVerify;
    @PostMapping("/verifyLocation")
    public ResponseEntity verifyLocation(@Valid @RequestBody VerifyAddressRequest req) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("%s/geocode/v1/json?q=%s&key=%s", urlVerify, URLEncoder.encode(req.getAddress(), "UTF-8"), apiKey);
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonResults = jsonObject.getJSONArray("results");
            JSONArray jsonArrayResponse = new JSONArray();

            for (Object i : jsonResults) {
                JSONObject jsonResponse = new JSONObject();
                JSONObject element = (JSONObject) i;
                try{
                    jsonResponse.put("bounds", element.getJSONObject("bounds"));
                    jsonResponse.put("formatted", element.getString("formatted"));
                }catch (Exception e){
                    continue;
                }
                jsonArrayResponse.put(jsonResponse);
            }
           if(jsonArrayResponse.isEmpty()){
               return ResponseEntity.status(200).body("The location not exist");
           }else{
               return ResponseEntity.status(200).body(jsonArrayResponse.toString());
           }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error code when fetching api");
        }
    }
    @PostMapping("/verifyCoordinates")
    public ResponseEntity verifyCoordinates(@Valid @RequestBody VerifyDDAddressRequest req) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = String.format("%s/geocode/v1/json?key=%s&q=%s+%s&prety=1", urlVerify, apiKey, req.getLat(), req.getLng());
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonResults = jsonObject.getJSONArray("results");
            JSONArray jsonArrayResponse = new JSONArray();

            for (Object i : jsonResults) {
                JSONObject jsonResponse = new JSONObject();
                JSONObject element = (JSONObject) i;
                try{
                    jsonResponse.put("bounds", element.getJSONObject("bounds"));
                    jsonResponse.put("formatted", element.getString("formatted"));
                }catch (Exception e){
                    continue;
                }
                jsonArrayResponse.put(jsonResponse);
            }
            if(jsonArrayResponse.isEmpty()){
                return ResponseEntity.status(200).body("The location not exist");
            }else{
                return ResponseEntity.status(200).body(jsonArrayResponse.toString());
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error code when fetching api");
        }
    }
}
