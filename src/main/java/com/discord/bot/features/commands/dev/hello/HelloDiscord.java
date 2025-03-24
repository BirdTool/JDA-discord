package com.discord.bot.features.commands.dev.hello;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.discord.bot.services.translate.JsonTranslationService;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;

public class HelloDiscord {
    /**
     * Manipulador para o subcomando "discord"
     * @param event O evento de interação do slash command
     * @throws IOException 
     */
    public void execute(SlashCommandInteractionEvent event) throws IOException {
        // Obtém o locale do usuário
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
        
        try {
            // Obtém a mensagem traduzida
            String message = translation.get("hello.subCommands.discord.response");
            
            // Envia a resposta traduzida
            event.reply(message).queue();
            
        } catch (Exception e) {
            // Fallback para uma mensagem padrão em caso de erro
            event.reply("Hello Discord! (Translation error occurred)").queue();
            e.printStackTrace();
        }
    }

    public String getPrincipalCommandName() {
        return "hello";
    }

    public String getSubCommandName() {
        return "discord";
    }
}
