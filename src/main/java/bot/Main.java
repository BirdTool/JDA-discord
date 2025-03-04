package bot;

import bot.base.BotInitializer;
import bot.base.Config;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logback config: " + ch.qos.logback.classic.LoggerContext.class.getProtectionDomain().getCodeSource().getLocation());
        String token = Config.get("TOKEN");
        BotInitializer.startBot(token);
    }
}
