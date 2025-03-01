package bot.base;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.List;

public class BotInitializer {

    public static void startBot(String token) {
        JDABuilder bot = JDABuilder.createDefault(token);

        // Lista para armazenar os dados dos comandos
        List<SlashCommandData> commandDataList = new ArrayList<>();

        // Pacote onde as classes estão localizadas
        String packageName = "bot.features";

        try {
            // Escaneia o pacote em busca de classes anotadas com @RegisterCommand
            List<Class<?>> classes = ClassScanner.getClassesWithAnnotation(packageName, RegisterCommand.class);

            for (Class<?> clazz : classes) {
                // Verifica se a classe é um ListenerAdapter
                if (ListenerAdapter.class.isAssignableFrom(clazz)) {
                    // Cria uma instância da classe e a adiciona como listener
                    ListenerAdapter listener = (ListenerAdapter) clazz.getDeclaredConstructor().newInstance();
                    bot.addEventListeners(listener);

                    // Verifica se a classe é um comando (tem o método getCommandData)
                    try {
                        if (clazz.getMethod("getCommandData") != null) {
                            SlashCommandData commandData = (SlashCommandData) clazz.getMethod("getCommandData").invoke(listener);
                            commandDataList.add(commandData);
                            System.out.println("Comando registrado: " + commandData.getName());
                        } else {
                            System.out.println("Listener carregado: " + clazz.getSimpleName());
                        }
                    } catch (NoSuchMethodException e) {
                        // Se a classe não tiver o método getCommandData, apenas registra como listener
                        System.out.println("Listener carregado: " + clazz.getSimpleName());
                    }
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

        // Registra os comandos globalmente
        jda.updateCommands().addCommands(commandDataList).queue();

        System.out.println("Iniciado como: " + jda.getSelfUser().getName());
        System.out.println("Bot iniciado e comandos registrados automaticamente!");
    }
}