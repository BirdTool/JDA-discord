package bot.base;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import bot.base.RegisterEvent;

import java.util.List;

public class BotInitializer {
    private static CommandManager commandManager = new CommandManager();
    private static InteractionManager interactionManager = new InteractionManager();
    private static EventManager eventManager = new EventManager();
    
    public static void startBot(String token) {
        JDABuilder bot = JDABuilder.createDefault(token);
        
        // Adiciona os gerenciadores como listeners
        bot.addEventListeners(commandManager);
        bot.addEventListeners(interactionManager);

        // Pacote onde as classes estão localizadas
        String packageName = "bot.features";

        try {
            // Escaneia o pacote em busca de classes anotadas
            List<Class<?>> commandClasses = ClassScanner.getClassesWithAnnotation(packageName, RegisterCommand.class);
            List<Class<?>> eventClasses = ClassScanner.getClassesWithAnnotation(packageName, RegisterEvent.class);
            
            // Processa as classes de comando
            for (Class<?> clazz : commandClasses) {
                // Verifica se a classe implementa Command
                if (Command.class.isAssignableFrom(clazz)) {
                    Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                    commandManager.registerCommand(command);
                }
                // Verifica se a classe implementa InteractionHandler
                else if (InteractionHandler.class.isAssignableFrom(clazz)) {
                    InteractionHandler<?> handler = (InteractionHandler<?>) clazz.getDeclaredConstructor().newInstance();
                    interactionManager.registerHandler(handler);
                }
                // Para compatibilidade com código existente
                else if (ListenerAdapter.class.isAssignableFrom(clazz)) {
                    ListenerAdapter listener = (ListenerAdapter) clazz.getDeclaredConstructor().newInstance();
                    eventManager.registerEventListener(listener);
                }
            }
            
            // Processa as classes de evento
            for (Class<?> clazz : eventClasses) {
                if (EventListener.class.isAssignableFrom(clazz)) {
                    EventListener listener = (EventListener) clazz.getDeclaredConstructor().newInstance();
                    eventManager.registerEventListener(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDA jda = bot.build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Registra todos os manipuladores de eventos
        eventManager.registerAllListeners(jda);

        // Registra os comandos globalmente
        jda.updateCommands().addCommands(commandManager.getCommandsData()).queue();

        System.out.println("Iniciado como: " + jda.getSelfUser().getName());
        System.out.println("Bot iniciado e comandos registrados automaticamente!");
    }
}
