package bot.features.commands.economia;

import bot.base.RegisterCommand;
import bot.base.Store;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@RegisterCommand
public class Saldo extends ListenerAdapter {
    private final Store<Number> moneyStore = new Store<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("rpg")) return;
        if (!event.getSubcommandGroup().equals("economia")) return;
        if (!event.getSubcommandName().equals("saldo")) return;

        /*
         *  Esse comando é um exemplo, eloe sempre retornará 0 pois não está
         * conectado a um banco de dados, e a classe store serve apenas para
         * guardar cooldowns não persistentes (Cooldowns pequenos de alguns segundos
         * até alguns minutos), mesmo podendo ser usado para guardar cooldowns
         * extremante grandes, se o bot cair o cooldown é resetado.
         */

        // Obtém o usuário alvo (se fornecido) ou o próprio usuário que executou o comando
        OptionMapping userOption = event.getOption("usuario");
        String userId = userOption != null
            ? userOption.getAsUser().getId() // Usuário fornecido
            : event.getUser().getId(); // Usuário que executou o comando

        // Verifica se o usuário tem um saldo registrado
        if (!moneyStore.has(userId)) {
            moneyStore.set(userId, 0); // Inicializa o saldo com 0
        }

        // Obtém o saldo do usuário
        Number saldo = moneyStore.get(userId);

        // Responde com o saldo
        event.reply("O saldo de <@" + userId + "> é: " + saldo + " moedas.").queue();
    }
}