package com.discord.bot.base.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ErrorEmbed {
    public static void sendSlashErrorEmbed(SlashCommandInteractionEvent event, String message) {
        event.replyEmbeds(getErrorEmbed(message)).setEphemeral(true).queue();
    }
    public static MessageEmbed getErrorEmbed(String message) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(java.awt.Color.decode(Colors.danger).getRGB());
        embed.setDescription("**❌ | Error: " + message + "**");
        return embed.build();
    }
}
