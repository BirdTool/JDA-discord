package bot.base;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.JDA;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventListener> eventListeners = new ArrayList<>();
    
    /**
     * Registra um manipulador de eventos
     */
    public void registerEventListener(EventListener listener) {
        eventListeners.add(listener);
        
        String description = "Evento";
        if (listener instanceof EventHandler) {
            description = ((EventHandler) listener).getEventDescription();
        } else if (listener instanceof ListenerAdapter) {
            description = listener.getClass().getSimpleName();
        }
        
        System.out.println("Manipulador de evento registrado: " + description);
    }
    
    /**
     * Registra todos os manipuladores de eventos no JDA
     */
    public void registerAllListeners(JDA jda) {
        for (EventListener listener : eventListeners) {
            jda.addEventListener(listener);
        }
    }
}
