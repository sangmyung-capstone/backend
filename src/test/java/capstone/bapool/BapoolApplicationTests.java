package capstone.bapool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BapoolApplicationTests {

	@Test
	void contextLoads() {

		String name = "wfwf";
		if (name.length() > 20){
			System.out.println("name = " + name);
		}
	}

}
