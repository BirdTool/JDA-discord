package bot.base;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface InteractionHandler<T extends GenericInteractionCreateEvent> {
    String getInteractionId();
    
    /**
     * Retorna o tipo de interação que este manipulador processa
     */
    InteractionType getInteractionType();
    
    /**
     * Processa a interação
     */
    void handleInteraction(T event);
    
    /**
     * Tipos de interação suportados
     */
    enum InteractionType {
        BUTTON,
        SELECT_MENU,
        MODAL,
        CONTEXT_MENU,
        AUTOCOMPLETE
    }
}
