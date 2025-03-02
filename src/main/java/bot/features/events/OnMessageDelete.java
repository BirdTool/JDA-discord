package bot.features.events;

import bot.base.RegisterCommand;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

@RegisterCommand
public class OnMessageDelete extends ListenerAdapter {
    public void onMessageDelete(MessageDeleteEvent event) {
        System.out.println("Mensagem deletada: " + event.getMessageId());
        try {
            event.getGuild().getTextChannelById("1178024982887014470")
                 .sendMessage("Mensagem deletada: " + event.getMessageId())
                 .queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
