package bot.features.commands.economia;

import bot.base.ErrorEmbedUtil;
import bot.base.RegisterCommand;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import bot.base.EasyEmbedUtil;
import bot.base.Colors;
import bot.Services.Database.DbManager;
import bot.Services.Database.DbManager.UserData;

@RegisterCommand
public class Trabalhar extends ListenerAdapter {
    private static final long COOLDOWN_DURATION = 1000L * 60 * 60 * 24; // 24 horas em milissegundos

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("rpg")) return;
        if (!event.getSubcommandGroup().equals("economia")) return;
        if (!event.getSubcommandName().equals("trabalhar")) return;

        String userId = event.getUser().getId();
        event.deferReply().queue();

        try {
            // Obtém ou cria o usuário no banco de dados
            UserData user = DbManager.getValue("users", userId);
            if (user == null) {
                user = new UserData();
            }

            // Acessa ou cria o mapa de cooldowns
            @SuppressWarnings("unchecked")
            Map<String, Long> cooldowns = (Map<String, Long>) user.getProperty("cooldowns");
            if (cooldowns == null) {
                cooldowns = new HashMap<>();
                user.setProperty("cooldowns", cooldowns);
            }

            // Verifica o cooldown de "work"
            Long workCooldownEnd = cooldowns.get("work");
            long currentTime = System.currentTimeMillis();

            if (workCooldownEnd != null && currentTime < workCooldownEnd) {
                long cooldownEndSeconds = workCooldownEnd / 1000;
                event.getHook().editOriginalEmbeds(
                    ErrorEmbedUtil.errorMessage("Você pode trabalhar novamente em: <t:" + cooldownEndSeconds + ":R>!")
                ).queue();
                return;
            }

            // Define o novo cooldown
            long cooldownEndTime = currentTime + COOLDOWN_DURATION;
            cooldowns.put("work", cooldownEndTime);
            user.setProperty("cooldowns", cooldowns);

            // Gera uma quantia aleatória de moedas
            Random random = new Random();
            int earnedMoney = random.nextInt(100, 501);

            // Atualiza o saldo
            Integer money = (Integer) user.getProperty("money");
            if (money == null) money = 0;
            money += earnedMoney;
            user.setProperty("money", money);
            DbManager.setValue("users", userId, user);

            // Responde com o resultado
            event.getHook().editOriginalEmbeds(
                EasyEmbedUtil.createEmbed("Trabalho", "Você trabalhou e ganhou **" + earnedMoney + "** moedas! Agora tem: **" + money + "** moedas", Colors.success)
            ).queue();

        } catch (Exception e) {
            event.getHook().editOriginalEmbeds(
                ErrorEmbedUtil.errorMessage("Ocorreu um erro ao trabalhar: " + (e.getMessage() != null ? e.getMessage() : "Erro desconhecido"))
            ).queue();
            e.printStackTrace();
        }
    }
}