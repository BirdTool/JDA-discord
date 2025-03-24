package com.discord.bot.features.commands.dev.hello;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.discord.bot.services.translate.JsonTranslationService;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class HelloRegister {
    
    /**
     * Retorna os dados do comando para registro
     * @return CommandData para o comando hello
     * @throws IOException 
     */
    public CommandData getCommandData() throws IOException {
        JsonTranslationService translation_en_us = new JsonTranslationService("en-us");
        JsonTranslationService translation_pt_br = new JsonTranslationService("pt-br");
        JsonTranslationService translation_es_es = new JsonTranslationService("es-es");

        // Comando principal
        String name_en_us = translation_en_us.get("hello.name");
        String name_pt_br = translation_pt_br.get("hello.name");
        String name_es_es = translation_es_es.get("hello.name");
        
        String description_en_us = translation_en_us.get("hello.description");
        String description_pt_br = translation_pt_br.get("hello.description");
        String description_es_es = translation_es_es.get("hello.description");
        
        // Subcomando world
        String world_name_en_us = translation_en_us.get("hello.subCommands.world.name");
        String world_name_pt_br = translation_pt_br.get("hello.subCommands.world.name");
        String world_name_es_es = translation_es_es.get("hello.subCommands.world.name");
        
        String world_desc_en_us = translation_en_us.get("hello.subCommands.world.description");
        String world_desc_pt_br = translation_pt_br.get("hello.subCommands.world.description");
        String world_desc_es_es = translation_es_es.get("hello.subCommands.world.description");
        
        // Subcomando user
        String user_name_en_us = translation_en_us.get("hello.subCommands.user.name");
        String user_name_pt_br = translation_pt_br.get("hello.subCommands.user.name");
        String user_name_es_es = translation_es_es.get("hello.subCommands.user.name");
        
        String user_desc_en_us = translation_en_us.get("hello.subCommands.user.description");
        String user_desc_pt_br = translation_pt_br.get("hello.subCommands.user.description");
        String user_desc_es_es = translation_es_es.get("hello.subCommands.user.description");


        // Subcomando discord
        String discord_name_en_us = translation_en_us.get("hello.subCommands.discord.name");
        String discord_name_pt_br = translation_pt_br.get("hello.subCommands.discord.name");
        String discord_name_es_es = translation_es_es.get("hello.subCommands.discord.name");
        
        String discord_desc_en_us = translation_en_us.get("hello.subCommands.discord.description");
        String discord_desc_pt_br = translation_pt_br.get("hello.subCommands.discord.description");
        String discord_desc_es_es = translation_es_es.get("hello.subCommands.discord.description");
        
        // Criando o comando com localizações
        SlashCommandData commandData = Commands.slash("hello", "Greets in different ways")
                .setNameLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, name_pt_br,
                        DiscordLocale.SPANISH, name_es_es
                    )
                )
                .setDescriptionLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, description_pt_br,
                        DiscordLocale.SPANISH, description_es_es
                    )
                );
        
        // Adicionando subcomandos com localizações
        SubcommandData worldSubcommand = new SubcommandData("world", "Says hello to the world")
                .setNameLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, world_name_pt_br,
                        DiscordLocale.SPANISH, world_name_es_es
                    )
                )
                .setDescriptionLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, world_desc_pt_br,
                        DiscordLocale.SPANISH, world_desc_es_es
                    )
                );
        
        SubcommandData userSubcommand = new SubcommandData("user", "Says hello to the user")
                .setNameLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, user_name_pt_br,
                        DiscordLocale.SPANISH, user_name_es_es
                    )
                )
                .setDescriptionLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, user_desc_pt_br,
                        DiscordLocale.SPANISH, user_desc_es_es
                    )
                );
        
        SubcommandData discordSubcommand = new SubcommandData("discord", "Says hello to the discord")
                .setNameLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, discord_name_pt_br,
                        DiscordLocale.SPANISH, discord_name_es_es
                    )
                )
                .setDescriptionLocalizations(
                    Map.of(
                        DiscordLocale.PORTUGUESE_BRAZILIAN, discord_desc_pt_br,
                        DiscordLocale.SPANISH, discord_desc_es_es
                    )
                );
        
        // Usando a forma original de adicionar subcomandos como no seu código
        List<SubcommandData> subcommands = new ArrayList<>();
        subcommands.add(worldSubcommand);
        subcommands.add(userSubcommand);
        subcommands.add(discordSubcommand);
        
        return commandData.addSubcommands(subcommands);
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
