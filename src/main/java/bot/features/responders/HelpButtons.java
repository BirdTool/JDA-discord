package bot.features.responders;

import bot.base.ButtonIdSeparator;
import bot.base.ErrorEmbedUtil;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import bot.features.menus.HelpMenu;
import bot.base.MenuResponse;
import bot.base.RegisterCommand;

@RegisterCommand
public class HelpButtons extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        try {
            // Separa o ID do botão em prefixo e valor
            String prefix = ButtonIdSeparator.getPart(buttonId, 0);
            String value = ButtonIdSeparator.getPart(buttonId, 1);

            // Verifica se o botão é do menu de ajuda
            if (!prefix.equals("helpMenu")) {
                return;
            }

            // Converte o valor para inteiro
            int page = Integer.parseInt(value);

            // Obtém a resposta do menu
            MenuResponse message = HelpMenu.MenuHelp(page);

            // Atualiza a mensagem original com o novo embed e botões
            event.editMessageEmbeds(message.getEmbed())
                 .setActionRow(message.getButtons())
                 .queue();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorEmbedUtil.sendErrorMessageButton(event, "Erro ao processar o botão");
        }
    }
}