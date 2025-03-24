package com.discord.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.discord.bot.base.env.Dotenv;

import com.discord.bot.base.Inicializer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    
    @Bean
    public CommandLineRunner initializeBot() {
		String TOKEN = Dotenv.get("TOKEN");
        return args -> {
            // Inicializar o bot após o Spring Boot iniciar
            Inicializer.botInicializer(TOKEN);
        };
    }
}
