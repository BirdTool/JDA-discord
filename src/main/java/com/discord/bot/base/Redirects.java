package com.discord.bot.base;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.discord.bot.base.utils.ErrorEmbed;

public class Redirects {
    // Cache para armazenar classes de comando já encontradas
    private static final Map<String, Class<?>> commandClassCache = new ConcurrentHashMap<>();
    private static final Set<Class<?>> allCommandClasses = new HashSet<>();
    private static final String FEATURES_PACKAGE = "com.discord.bot.features";

    // Inicialização estática para carregar todas as classes de comando
    static {
        loadAllCommandClasses();
    }

    /**
     * Carrega todas as classes dentro do pacote features recursivamente
     */
    private static void loadAllCommandClasses() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String packagePath = FEATURES_PACKAGE.replace('.', '/');
            URL resource = classLoader.getResource(packagePath);
            
            if (resource == null) {
                System.err.println("Features package not found!");
                return;
            }

            File directory = new File(resource.getFile());
            if (directory.exists()) {
                scanDirectory(directory, FEATURES_PACKAGE);
            }
        } catch (Exception e) {
            System.err.println("Error loading command classes:");
            e.printStackTrace();
        }
    }

    /**
     * Escaneia um diretório recursivamente em busca de classes
     */
    private static void scanDirectory(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    // Adicionar ao conjunto se for uma classe potencial de comando
                    if (isPotentialCommandClass(clazz)) {
                        allCommandClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Could not load class: " + className);
                }
            }
        }
    }

    /**
     * Verifica se uma classe é potencialmente um handler de comando
     */
    private static boolean isPotentialCommandClass(Class<?> clazz) {
        try {
            return clazz.getDeclaredMethod("execute", SlashCommandInteractionEvent.class) != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Redireciona um evento de comando slash para o manipulador apropriado
     */
    public static void redirectSlashCommands(SlashCommandInteractionEvent event) {
        try {
            String commandName = event.getName().toLowerCase();
            String subcommandGroup = event.getSubcommandGroup();
            String subcommand = event.getSubcommandName();

            System.out.println("Received command: " + commandName +
                    (subcommandGroup != null ? " with group: " + subcommandGroup : "") +
                    (subcommand != null ? " with subcommand: " + subcommand : ""));

            Class<?> handlerClass = findMatchingCommandClass(commandName, subcommandGroup, subcommand);

            if (handlerClass == null) {
                System.err.println("Command handler class not found for: " + commandName);
                ErrorEmbed.sendSlashErrorEmbed(event, "Command handler not found.");
                return;
            }

            // Criar uma instância da classe do manipulador
            Object handlerInstance = handlerClass.getDeclaredConstructor().newInstance();

            // Verificar nomes esperados do comando
            String principalCommandName = getMethodString(handlerClass, handlerInstance, "getPrincipalCommandName");
            String subCommandGroupName = getMethodString(handlerClass, handlerInstance, "getSubCommandGroupName");
            String subCommandName = getMethodString(handlerClass, handlerInstance, "getSubCommandName");

            // Verificar se esta classe deve manipular este comando
            if (!isCommandMatch(commandName, subcommandGroup, subcommand, 
                              principalCommandName, subCommandGroupName, subCommandName)) {
                System.err.println("Command handler mismatch: " + handlerClass.getName());
                ErrorEmbed.sendSlashErrorEmbed(event, "Command handler mismatch.");
                return;
            }

            // Invocar o método execute
            Method executeMethod = handlerClass.getDeclaredMethod("execute", SlashCommandInteractionEvent.class);
            executeMethod.invoke(handlerInstance, event);

        } catch (Exception e) {
            System.err.println("Error redirecting slash command:");
            e.printStackTrace();
            ErrorEmbed.sendSlashErrorEmbed(event, "An error occurred while processing your command.");
        }
    }

    /**
     * Encontra a classe de comando correspondente
     */
    private static Class<?> findMatchingCommandClass(String commandName, String subcommandGroup, String subcommand) {
        String cacheKey = commandName + (subcommandGroup != null ? "." + subcommandGroup : "") + 
                        (subcommand != null ? "." + subcommand : "");

        // Verificar cache
        if (commandClassCache.containsKey(cacheKey)) {
            return commandClassCache.get(cacheKey);
        }

        // Procurar em todas as classes carregadas
        for (Class<?> clazz : allCommandClasses) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                String principal = getMethodString(clazz, instance, "getPrincipalCommandName");
                String group = getMethodString(clazz, instance, "getSubCommandGroupName");
                String sub = getMethodString(clazz, instance, "getSubCommandName");

                if (principal != null && principal.equalsIgnoreCase(commandName) &&
                    (group == null || group.equalsIgnoreCase(subcommandGroup)) &&
                    (sub == null || sub.equalsIgnoreCase(subcommand))) {
                    commandClassCache.put(cacheKey, clazz);
                    return clazz;
                }
            } catch (Exception e) {
                // Continuar para a próxima classe em caso de erro
            }
        }
        return null;
    }

    /**
     * Obtém o resultado de um método String, se existir
     */
    private static String getMethodString(Class<?> clazz, Object instance, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            return (String) method.invoke(instance);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica se o comando corresponde aos nomes esperados
     */
    private static boolean isCommandMatch(String commandName, String subcommandGroup, String subcommand,
                                        String principal, String group, String sub) {
        if (principal != null && !principal.equalsIgnoreCase(commandName)) {
            return false;
        }
        if (group != null && (subcommandGroup == null || !group.equalsIgnoreCase(subcommandGroup))) {
            return false;
        }
        if (sub != null && (subcommand == null || !sub.equalsIgnoreCase(subcommand))) {
            return false;
        }
        return true;
    }
}