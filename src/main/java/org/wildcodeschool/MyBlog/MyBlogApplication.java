package org.wildcodeschool.MyBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.MyBlog.config.EnvConfig;

@SpringBootApplication

public class MyBlogApplication {

	public static void main(String[] args) {

		SpringApplication.run(MyBlogApplication.class, args);
		String username = EnvConfig.get("MYSQL_USERNAME");
		String password = EnvConfig.get("MYSQL_PASSWORD");
		System.out.println("MySQL Username: " + username);

	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}

}
