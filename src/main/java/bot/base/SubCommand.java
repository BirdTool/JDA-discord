package bot.base;

public interface SubCommand extends Command {
    /**
     * Retorna o nome do comando principal
     */
    String getMainCommandName();
    
    /**
     * Retorna o nome do grupo de subcomandos (opcional)
     */
    default String getSubcommandGroup() {
        return null;
    }
    
    /**
     * Retorna o nome do subcomando
     */
    String getSubcommandName();
}
