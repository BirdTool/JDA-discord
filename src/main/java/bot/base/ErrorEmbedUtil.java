package bot.base;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ErrorEmbedUtil {
    // Método para criar um Embed de erro
    public static MessageEmbed errorMessage(String error) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("**❌ | " + error + "**");
        embedBuilder.setColor(java.awt.Color.decode("#d22b2b"));
        return embedBuilder.build();
    }

    // Método para enviar uma mensagem de erro
    public static void sendErrorMessage(SlashCommandInteractionEvent event, String error) {
        event.replyEmbeds(errorMessage(error))
             .setEphemeral(true)
             .queue(); // Envia a mensagem
    }

    public static void sendErrorMessageButton(ButtonInteractionEvent event, String error) {
        event.replyEmbeds(errorMessage(error))
             .setEphemeral(true)
             .queue(); // Envia a mensagem
    }

    // Método para criar um Embed de aviso
    public static MessageEmbed warningMessage(String error) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(error);
        embedBuilder.setColor(java.awt.Color.decode("#f7d33e"));
        return embedBuilder.build();
    }
}
