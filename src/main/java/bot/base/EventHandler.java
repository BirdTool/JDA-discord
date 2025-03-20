package bot.base;

import java.util.EventListener;

/**
 * Interface para manipuladores de eventos do JDA
 * Estende ListenerAdapter para herdar todos os métodos de eventos
 */
public interface EventHandler extends EventListener {
    /**
     * Retorna o tipo de evento que este manipulador processa
     */
    Class<?> getEventType();
    
    /**
     * Retorna uma descrição do evento para fins de log
     */
    default String getEventDescription() {
        return getEventType().getSimpleName();
    }
}
