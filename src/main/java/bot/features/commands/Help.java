package bot.features.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import bot.base.Command;
import bot.base.MenuResponse;
import bot.base.RegisterCommand;
import bot.features.menus.HelpMenu;

@RegisterCommand
public class Help implements Command {
    public void execute(SlashCommandInteractionEvent event) {
        MenuResponse message = HelpMenu.MenuHelp(0);

        event.replyEmbeds(message.getEmbed())
             .setEphemeral(message.isEphemeral())
             .addActionRow(message.getButtons())
             .queue();
    }

    public SlashCommandData getCommandData() {
        return Commands.slash("help", "Ajuda com o bot");
    }
}
