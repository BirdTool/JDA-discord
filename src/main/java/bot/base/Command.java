package bot.base;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface Command {
    /**
     * Executa o comando
     */
    void execute(SlashCommandInteractionEvent event);
    
    /**
     * Retorna os dados do comando (opcional para subcomandos)
     * Implementações de subcomandos podem retornar null
     */
    default SlashCommandData getCommandData() {
        return null;
    }
    
    /**
     * Verifica se este comando é um comando principal (não um subcomando)
     */
    default boolean isMainCommand() {
        return getCommandData() != null;
    }
}
