package bot.features.responders;

import java.util.Collections;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import bot.base.InteractionHandler;
import bot.base.RegisterCommand;

@RegisterCommand
public class BotaoResponder implements InteractionHandler<ButtonInteractionEvent> {

    @Override
    public String getInteractionId() {
        return "botao_exemplo_id";
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.BUTTON;
    }

    @Override
    public void handleInteraction(ButtonInteractionEvent event) {
        // Atualiza a mensagem original: remove o embed e o botão, e define um novo conteúdo
        event.editMessage("Você clicou no botão!") // Define o novo conteúdo
             .setEmbeds(Collections.emptyList()) // Remove todos os embeds
             .setComponents(Collections.emptyList()) // Remove todos os componentes (botões)
             .queue(); // Envia as alterações
    }
}
