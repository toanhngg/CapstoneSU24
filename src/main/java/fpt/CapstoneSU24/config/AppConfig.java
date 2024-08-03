package fpt.CapstoneSU24.config;

import fpt.CapstoneSU24.service.BlockchainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public BlockchainService blockchainService() {
        return new BlockchainService();
    }
}