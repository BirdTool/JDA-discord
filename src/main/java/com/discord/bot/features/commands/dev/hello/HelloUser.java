package com.discord.bot.features.commands.dev.hello;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.discord.bot.services.translate.JsonTranslationService;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;

public class HelloUser {
    /**
     * Manipulador para o subcomando "user"
     * @param event O evento de interação do slash command
     * @throws IOException 
     */
    public void execute(SlashCommandInteractionEvent event) throws IOException {
        DiscordLocale userLocale = event.getUserLocale();
        
        // Mapeia o DiscordLocale para o código de idioma usado nos arquivos de tradução
        String locale;
        if (userLocale == DiscordLocale.PORTUGUESE_BRAZILIAN) {
            locale = "pt-br";
        } else if (userLocale == DiscordLocale.SPANISH) {
            locale = "es-es";
        } else {
            locale = "en-us";
        }
        
        // Carrega o serviço de tradução com o idioma correto
        JsonTranslationService translation = new JsonTranslationService(locale);

        String mention = event.getUser().getAsMention();

        Map<String, String> replacements = new HashMap<>();
        replacements.put("user", mention);

        String message = translation.get("hello.subCommands.user.response", replacements);

        event.reply(message).queue();
    }

    public String getPrincipalCommandName() {
        return "hello";
    }

    public String getSubCommandName() {
        return "user";
    }
}
