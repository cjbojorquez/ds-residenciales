package gt.com.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication

public class ResidencialesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResidencialesApplication.class, args);
	}

}
