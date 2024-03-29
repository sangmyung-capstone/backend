package capstone.bapool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BapoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(BapoolApplication.class, args);
	}

}
