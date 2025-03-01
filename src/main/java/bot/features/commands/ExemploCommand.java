package bot.features.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;

import bot.base.Store;
import bot.base.ErrorEmbedUtil;
import bot.base.RegisterCommand;

@RegisterCommand
public class ExemploCommand extends ListenerAdapter {
    private final Store<Long> cooldownStore = new Store<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("exemplo")) return;
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
                ErrorEmbedUtil.sendErrorMessage(event, "Você pode usar esse comando novamente <t:" + cooldownEndTimeSeconds + ":R>!");
                return;
            } else {
                // Se o cooldown já terminou, remove o usuário do cooldownStore
                cooldownStore.remove(userId);
                System.out.println("O usuário foi removido da store");
            }
        }

        // Define um cooldown de 2 minutos para o usuário
        long cooldownTime = 1000L * 60 * 2; // 2 minutos em milissegundos
        long cooldownEndTime = System.currentTimeMillis() + cooldownTime; // Tempo de término do cooldown
        cooldownStore.set(userId, cooldownEndTime, cooldownTime);

        // Verifica se o comando é "exemplo"
        if (!event.getName().equals("exemplo")) return;

        // Cria o embed e o botão
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Título do Embed");
        embed.setDescription("Esta é uma descrição de exemplo.");
        embed.setColor(Color.BLUE);

        Button button = Button.primary("botao_exemplo_id", "Clique Aqui");

        // Responde ao comando
        event.replyEmbeds(embed.build())
             .addActionRow(button)
             .setEphemeral(true)
             .queue();
    }

    public SlashCommandData getCommandData() {
        return Commands.slash("exemplo", "Comando de exemplo");
    }
}