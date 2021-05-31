package com.example.globallogic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig  {

	@Bean
	CommandLineRunner commandLineRunner(MyRepository repository) {
		return args -> {
			GlobalLogic g = new GlobalLogic("logic","I love to work in global logic!",  "{(i), 1} = 0.07 (1/15)\r\n"
					+ "{(i), 2} = 0.07 (1/15)\r\n"
					+ "{(o), 2} = 0.07 (1/15)\r\n"
					+ "{(o), 4} = 0.07 (1/15)\r\n"
					+ "{(l, o), 4} = 0.13 (2/15)\r\n"
					+ "{(l, o, g), 6} = 0.27 (4/15)\r\n"
					+ "{(l, o, g, i, c), 5} = 0.33 (5/15)\r\n"
					+ "TOTAL Frequency: 0.63 (15/24)\r\n"
					+ "");
			repository.save(g);
		};
	}
}
