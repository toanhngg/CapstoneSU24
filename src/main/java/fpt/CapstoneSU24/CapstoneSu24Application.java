package fpt.CapstoneSU24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = "fpt.CapstoneSU24.model")
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CapstoneSu24Application{
	public static void main(String[] args) {
		SpringApplication.run(CapstoneSu24Application.class, args);
	}

}
