package bot;

import bot.base.BotInitializer;
import bot.base.Config;

public class Main {
    public static void main(String[] args) {
        String token = Config.get("TOKEN");
        BotInitializer.startBot(token);
    }
}
