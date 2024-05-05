package fpt.CapstoneSU24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class CapstoneSu24Application{

	public static void main(String[] args) {
		SpringApplication.run(CapstoneSu24Application.class, args);
	}

}
