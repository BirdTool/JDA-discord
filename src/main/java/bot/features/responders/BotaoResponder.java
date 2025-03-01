package bot.features.responders;

import java.util.Collections;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import bot.base.RegisterCommand;

@RegisterCommand
public class BotaoResponder extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("botao_exemplo_id")) {
            // Atualiza a mensagem original: remove o embed e o botão, e define um novo conteúdo
            event.editMessage("Você clicou no botão!") // Define o novo conteúdo
                 .setEmbeds(Collections.emptyList()) // Remove todos os embeds
                 .setComponents(Collections.emptyList()) // Remove todos os componentes (botões)
                 .queue(); // Envia as alterações
        }
    }
}