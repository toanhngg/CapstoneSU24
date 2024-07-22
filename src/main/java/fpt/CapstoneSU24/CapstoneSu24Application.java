package fpt.CapstoneSU24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@ComponentScan(basePackages = "fpt.CapstoneSU24.model")
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CapstoneSu24Application{
	public static void main(String[] args) {
		SpringApplication.run(CapstoneSu24Application.class, args);
	}
}
