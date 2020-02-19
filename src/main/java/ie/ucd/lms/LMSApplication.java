package ie.ucd.lms;

import java.sql.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LMSApplication {
	public static void main(String[] args) throws SQLException {
		SpringApplication.run(LMSApplication.class, args);
	}
}
