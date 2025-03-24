package com.discord.bot.features.commands.dev.hello;

import java.io.IOException;

import com.discord.bot.services.translate.JsonTranslationService;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import com.discord.bot.base.utils.CommandTranslationHelper;

public class HelloRegister {
    
    /**
     * Retorna os dados do comando para registro
     * @return CommandData para o comando hello
     * @throws IOException 
     */
    public SlashCommandData getCommandData() throws IOException {
        CommandTranslationHelper helper = new CommandTranslationHelper();

        SlashCommandData command = helper.buildCommand("hello", "hello");
        helper.addSubcommand(command, "hello.subCommands.world");
        helper.addSubcommand(command, "hello.subCommands.user");
        helper.addSubcommand(command, "hello.subCommands.discord");

        return command;
    }

    public void execute(SlashCommandInteractionEvent event) throws IOException {
        // Obtém o idioma do usuário ou usa o padrão
        String locale = event.getUserLocale().toString().toLowerCase();
        if (!locale.equals("pt-br") && !locale.equals("es-es")) {
            locale = "en-us"; // Padrão para inglês
        }
        
        JsonTranslationService translation = new JsonTranslationService(locale);
        
        // Responde com a mensagem traduzida
        String response = "Olá! Use um dos subcomandos disponíveis.";
        try {
            response = translation.get("hello.defaultResponse");
        } catch (Exception e) {
            // Fallback para mensagem padrão se a chave não existir
        }
        
        event.reply(response).queue();
    }
}
