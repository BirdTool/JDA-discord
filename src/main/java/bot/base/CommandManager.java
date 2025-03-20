package bot.base;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, Map<String, Command>> subcommands = new HashMap<>();
    private final Map<String, Map<String, Map<String, Command>>> subcommandGroups = new HashMap<>();
    
    public List<SlashCommandData> getCommandsData() {
        List<SlashCommandData> commandDataList = new ArrayList<>();
        for (Command command : commands.values()) {
            if (command.isMainCommand()) {
                commandDataList.add(command.getCommandData());
            }
        }
        return commandDataList;
    }
    
    public void registerCommand(Command command) {
        if (command instanceof SubCommand) {
            registerSubCommand((SubCommand) command);
            return;
        }
        
        if (!command.isMainCommand()) {
            System.out.println("Aviso: Comando sem dados de comando: " + command.getClass().getSimpleName());
            return;
        }
        
        String commandName = command.getCommandData().getName();
        commands.put(commandName, command);
        
        // Inicializa mapas para possíveis subcomandos
        if (!subcommands.containsKey(commandName)) {
            subcommands.put(commandName, new HashMap<>());
        }
        if (!subcommandGroups.containsKey(commandName)) {
            subcommandGroups.put(commandName, new HashMap<>());
        }
        
        System.out.println("Comando registrado: " + commandName);
    }
    
    private void registerSubCommand(SubCommand subCommand) {
        String commandName = subCommand.getMainCommandName();
        String groupName = subCommand.getSubcommandGroup();
        String subcommandName = subCommand.getSubcommandName();
        
        if (groupName != null && !groupName.isEmpty()) {
            registerSubcommandGroup(commandName, groupName, subcommandName, subCommand);
        } else {
            registerSubcommand(commandName, subcommandName, subCommand);
        }
    }
    
    public void registerSubcommand(String commandName, String subcommandName, Command handler) {
        if (!subcommands.containsKey(commandName)) {
            subcommands.put(commandName, new HashMap<>());
        }
        subcommands.get(commandName).put(subcommandName, handler);
        System.out.println("Subcomando registrado: " + commandName + " " + subcommandName);
    }
    
    public void registerSubcommandGroup(String commandName, String groupName, String subcommandName, Command handler) {
        if (!subcommandGroups.containsKey(commandName)) {
            subcommandGroups.put(commandName, new HashMap<>());
        }
        if (!subcommandGroups.get(commandName).containsKey(groupName)) {
            subcommandGroups.get(commandName).put(groupName, new HashMap<>());
        }
        subcommandGroups.get(commandName).get(groupName).put(subcommandName, handler);
        System.out.println("Subcomando em grupo registrado: " + commandName + " " + groupName + " " + subcommandName);
    }
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        
        // Verifica se é um subcomando em um grupo
        if (event.getSubcommandGroup() != null && event.getSubcommandName() != null) {
            String groupName = event.getSubcommandGroup();
            String subcommandName = event.getSubcommandName();
            
            if (subcommandGroups.containsKey(commandName) && 
                subcommandGroups.get(commandName).containsKey(groupName) && 
                subcommandGroups.get(commandName).get(groupName).containsKey(subcommandName)) {
                
                subcommandGroups.get(commandName).get(groupName).get(subcommandName).execute(event);
                return;
            }
        }
        // Verifica se é um subcomando
        else if (event.getSubcommandName() != null) {
            String subcommandName = event.getSubcommandName();
            
            if (subcommands.containsKey(commandName) && 
                subcommands.get(commandName).containsKey(subcommandName)) {
                
                subcommands.get(commandName).get(subcommandName).execute(event);
                return;
            }
        }
        // Comando principal
        else if (commands.containsKey(commandName)) {
            commands.get(commandName).execute(event);
            return;
        }
        
        // Se chegou aqui, o comando não foi encontrado
        event.reply("Comando não encontrado ou não implementado corretamente.").setEphemeral(true).queue();
    }
}
