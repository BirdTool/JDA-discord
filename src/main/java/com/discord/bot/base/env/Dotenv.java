package com.discord.bot.base.env;

public class Dotenv {
    private static final io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
    
    public static String get(String key) {
        return dotenv.get(key);
    }
}