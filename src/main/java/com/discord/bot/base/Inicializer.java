package com.discord.bot.base;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import com.discord.bot.base.register.commands.RegisterCommands;

import java.util.List;

public class Inicializer {
    public static void botInicializer(String TOKEN) {
        try {
            // Construir o JDA com o listener para comandos slash
            JDA jda = JDABuilder.createDefault(TOKEN)
                    .addEventListeners(new ListenerAdapter() {
                        @Override
                        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                            // Redirecionar para o comando apropriado
                            Redirects.redirectSlashCommands(event);
                        }
                    })
                    .build();
            
            // Aguardar o JDA estar pronto
            jda.awaitReady();
            
            // Obter todos os comandos das classes de features
            List<CommandData> allCommands = RegisterCommands.getAllCommandData();
            
            // Registrar os comandos em slash
            jda.updateCommands().addCommands(allCommands).queue();
            
            System.out.println("Registered " + allCommands.size() + " slash commands successfully!");
            
        } catch (Exception e) {
            System.err.println("Error initializing the bot:");
            e.printStackTrace();
        }
    }
}
