package bot.features.events;

import bot.base.RegisterEvent;
import bot.base.EventHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

@RegisterEvent
public class OnMessageDelete extends ListenerAdapter implements EventHandler {
    @Override
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
    
    @Override
    public Class<?> getEventType() {
        return MessageDeleteEvent.class;
    }
    
    @Override
    public String getEventDescription() {
        return "Manipulador de exclusão de mensagens";
    }
}
