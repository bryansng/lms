package ie.ucd.lms;

import java.sql.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan("ie.ucd.lms")
public class LMSApplication {
	public static void main(String[] args) throws SQLException {
		SpringApplication.run(LMSApplication.class, args);
	}
}
