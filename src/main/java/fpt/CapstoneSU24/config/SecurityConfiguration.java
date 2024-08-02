package fpt.CapstoneSU24.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    JwtRequestFilter jwtRequestFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf ->csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/auth/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated())
//                .sessionManagement(sess -> sess
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/api/location/**",
                                "/webjars/**",
                                "/api/itemlog/getItemLogDetail",
                                "/api/item/viewOrigin",
                                "/api/item/viewLineItem",
                                "/api/item/authorized",
                                "/api/item/checkAuthorized",
                                "/api/item/checkCurrentOwner",
                                "/api/item/sendOTP",
                                "/api/item/confirmOTP",
                                "api/itemlog/additemlogTransport",
                                "api/transport/getAllTransport",
                                "api/item/sendCurrentOwnerOTP",
                                "api/item/confirmCurrentOwner",
                                "api/user/getUserById",
                                "api/item/getTESTCertificate",
                                "api/item/convertHtmlToPdf",
                                "api/item/getCertificate",
                                "api/item/abortItem",
                                "api/itemlog/editItemLog",
                                "api/test/healthCheck",
                                "api/category/findAll",
                                "/api/certificate/getListCertificateByManuId",
                                "/api/user/getDetailUser",
                                "/api/user/getAllUser",
                                "api/item/getItemByEventType",
                                "/api/auth/checkMailExist",
                                "/api/auth/checkOrgNameExist",
                                "/api/category/search",
                                "/api/category/findCategoryByManufacturer",
                                "/api/product/ViewProductByManufacturerId",
                                "/api/product/findProductIdByName",
                                "/api/certificate/ViewCertByManufacturerId",
                                "/api/elk/getNumberVisitsAllTime",
                                "/api/elk/getNumberVisitsDiagram",
                                "/api/user/getAllUser",
                                "api/item/getItemByEventType",
                                "api/item/check",
                                "api/item/checkEventAuthorized",
                                "/api/user/getManufacturerByProductId",
                                "/api/user/searchAllManufacturer",
                                "/api/product/countRegisteredProduct",
                                "/api/user/countRegisteredUser",
                                "/app/send/**",
                                "/app/topic/messages",
                                "/api/report/createReport",
                                "/topic/messages",
                                "/ws/**",
                                "api/item/logMetrics",
                                "api/customercare/add",
                                "api/user/top5OrgNames",
                                "api/customercare/searchCustomerCare"

                        ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://trace-origin.netlify.app"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("GET","POST", "PUT"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}