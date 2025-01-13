package org.wildcodeschool.MyBlog.config;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    public static Dotenv dotenv = Dotenv.configure().load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}