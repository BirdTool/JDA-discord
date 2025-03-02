package bot.features.commands.economia;

import bot.base.RegisterCommand;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@RegisterCommand
public class EconomyCommandsRegister extends ListenerAdapter {
    public SlashCommandData getCommandData() {
        return Commands.slash("rpg", "Comandos de rpg").addSubcommandGroups(
            new SubcommandGroupData("economia", "comandos de economia").addSubcommands(
                new SubcommandData("trabalhar", "Trabalhar para ganhar dinheiro"),
                new SubcommandData("saldo", "Mostra o seu saldo").addOptions(
                    new OptionData(OptionType.USER, "usuario", "Usuário que deseja ver o saldo").setRequired(false)
                )
            )
        );
    }
}
