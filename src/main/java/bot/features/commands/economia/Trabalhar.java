package bot.features.commands.economia;

import bot.base.ErrorEmbedUtil;
import bot.base.RegisterCommand;
import bot.base.Store;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.Random;

import bot.base.EasyEmbedUtil;

@RegisterCommand
public class Trabalhar extends ListenerAdapter {
    private final Store<Long> cooldownStore = new Store<>();
    private final Store<Number> moneyStore = new Store<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("rpg")) return;
        if (!event.getSubcommandGroup().equals("economia")) return;
        if (!event.getSubcommandName().equals("trabalhar")) return;

        String userId = event.getUser().getId();

        // Verifica se o usuário está em cooldown
        if (cooldownStore.has(userId)) {
            long cooldownEndTime = cooldownStore.get(userId); // Tempo de término do cooldown em milissegundos
            long currentTime = System.currentTimeMillis(); // Tempo atual em milissegundos

            // Se o cooldown ainda não terminou
            if (currentTime < cooldownEndTime) {
                // Converte o tempo restante para segundos
                long cooldownEndTimeSeconds = cooldownEndTime / 1000;
                // Envia a mensagem de erro com o tempo restante
                ErrorEmbedUtil.sendErrorMessage(event, "Você pode trabalhar novamente em: <t:" + cooldownEndTimeSeconds + ":R>!");
                return;
            } else {
                // Se o cooldown já terminou, remove o usuário do cooldownStore
                cooldownStore.remove(userId);
            }
        }

        // Define um cooldown de 24 horas para o usuário
        long cooldownTime = 1000L * 60 * 60 * 24; // 24 horas em milissegundos
        long cooldownEndTime = System.currentTimeMillis() + cooldownTime; // Tempo de término do cooldown
        cooldownStore.set(userId, cooldownEndTime);

        // Verifica se o usuário tem um saldo registrado
        if (!moneyStore.has(userId)) {
            moneyStore.set(userId, 0); // Inicializa o saldo com 0
        }

        // Obtém o saldo atual do usuário
        Number money = moneyStore.get(userId);

        // Gera uma quantia aleatória de moedas
        Random random = new Random();
        Number earnedMoney = random.nextInt(100, 501);

        // Atualiza o saldo do usuário
        moneyStore.set(userId, money.intValue() + earnedMoney.intValue());

        // Responde com o resultado do trabalho
        event.replyEmbeds(EasyEmbedUtil.createEmbed("Trabalho", "Você trabalhou e ganhou " + earnedMoney + " moedas!", "#1ec721")).queue();
    }
}