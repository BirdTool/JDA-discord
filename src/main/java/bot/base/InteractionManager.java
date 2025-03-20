package bot.base;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.base.InteractionHandler.InteractionType;

public class InteractionManager extends ListenerAdapter {
    private final Map<InteractionType, List<InteractionHandler<?>>> handlers = new HashMap<>();
    
    public InteractionManager() {
        // Inicializa as listas para cada tipo de interação
        for (InteractionType type : InteractionType.values()) {
            handlers.put(type, new ArrayList<>());
        }
    }
    
    public <T extends GenericInteractionCreateEvent> void registerHandler(InteractionHandler<T> handler) {
        handlers.get(handler.getInteractionType()).add(handler);
        System.out.println("Manipulador de interação registrado: " + 
                          handler.getInteractionType() + " - " + 
                          handler.getInteractionId());
    }
    
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        handleInteraction(event, InteractionType.BUTTON);
    }
    
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        handleInteraction(event, InteractionType.SELECT_MENU);
    }
    
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        handleInteraction(event, InteractionType.MODAL);
    }
    
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        handleInteraction(event, InteractionType.AUTOCOMPLETE);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends GenericInteractionCreateEvent> void handleInteraction(T event, InteractionType type) {
        String id = getInteractionId(event);
        if (id == null) return;
        
        for (InteractionHandler<?> handler : handlers.get(type)) {
            String pattern = handler.getInteractionId();
            
            if (IdMatcher.matches(pattern, id)) {
                try {
                    ((InteractionHandler<T>) handler).handleInteraction(event);
                    return;
                } catch (ClassCastException e) {
                    System.err.println("Erro ao processar interação: " + e.getMessage());
                }
            }
        }
        
        // Se chegou aqui, nenhum manipulador foi encontrado
        if (event instanceof GenericComponentInteractionCreateEvent) {
            ((GenericComponentInteractionCreateEvent) event)
                .reply("Nenhum manipulador encontrado para esta interação.")
                .setEphemeral(true)
                .queue();
        }
    }
    
    private String getInteractionId(GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            return ((ButtonInteractionEvent) event).getComponentId();
        } else if (event instanceof StringSelectInteractionEvent) {
            return ((StringSelectInteractionEvent) event).getComponentId();
        } else if (event instanceof ModalInteractionEvent) {
            return ((ModalInteractionEvent) event).getModalId();
        } else if (event instanceof CommandAutoCompleteInteractionEvent) {
            return ((CommandAutoCompleteInteractionEvent) event).getName();
        }
        return null;
    }
}
