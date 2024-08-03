package fpt.CapstoneSU24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

//@EnableScheduling
//@SpringBootApplication
//@ComponentScan(basePackages = "fpt.CapstoneSU24.model")
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CapstoneSu24Application{
	public static void main(String[] args) {

		SpringApplication.run(CapstoneSu24Application.class, args);
	}
	// Kết nối đến Infura hoặc một node khác
	@Bean
	public Web3j web3j() {
		return Web3j.build(new HttpService("https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID"));
	}
//	@Bean
//	public Web3j web3j() {
//		return Web3j.build(new HttpService("https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID"));
//	}
//	@Bean
//	public Credentials credentials() {
//		// Tạo đối tượng Credentials với private key
//		return Credentials.create("YOUR_PRIVATE_KEY");
//	}
}
