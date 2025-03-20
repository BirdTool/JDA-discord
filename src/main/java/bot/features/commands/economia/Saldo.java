package bot.features.commands.economia;

import bot.base.RegisterCommand;
import bot.base.SubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import bot.Services.Database.DbManager;
import bot.Services.Database.DbManager.UserData;
import bot.base.Colors;
import bot.base.EasyEmbedUtil;
import bot.base.ErrorEmbedUtil;

@RegisterCommand
public class Saldo implements SubCommand {
    @Override
    public String getMainCommandName() {
        return "rpg";
    }

    @Override
    public String getSubcommandGroup() {
        return "economia";
    }

    @Override
    public String getSubcommandName() {
        return "saldo";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        try {
            // Obtém o usuário alvo (se fornecido) ou o próprio usuário que executou o comando
            OptionMapping userOption = event.getOption("usuario");
            String userId = userOption != null
                ? userOption.getAsUser().getId() // Usuário fornecido
                : event.getUser().getId(); // Usuário que executou o comando
    
            if (userOption != null && userOption.getAsUser().isBot()) {
                event.getHook().editOriginalEmbeds(
                    ErrorEmbedUtil.errorMessage("Você não pode verificar o saldo de um bot.")
                ).queue();
                return;
            }
    
            int saldo;
    
            UserData retrievedUser = DbManager.getValue("users", userId);
            if (retrievedUser != null) {
                Integer money = (Integer) retrievedUser.getProperty("money");
                if (money != null) {
                    saldo = money;
                } else {
                    saldo = 0;
                }
            } else {
                saldo = 0;
            }
    
            // Responde com o saldo
            event.getHook().editOriginalEmbeds(
                EasyEmbedUtil.createEmbed("Saldo", "O saldo de <@" + userId + "> é: **" + saldo + "** moedas.", Colors.success)
            ).queue();
        } catch (Exception e) {
            event.getHook().editOriginalEmbeds(
                ErrorEmbedUtil.errorMessage("Ocorreu um erro ao verificar o saldo: " + e.getMessage())
            ).queue();

            e.printStackTrace();
        }
    }
}