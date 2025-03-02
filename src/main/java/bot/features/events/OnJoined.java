package bot.features.events;

import bot.base.RegisterCommand;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

@RegisterCommand
public class OnJoined extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String guildId = event.getGuild().getId();
        System.out.println("Evento de entrada detectado no servidor: " + guildId);

        if (!guildId.equals("1172930138770526248")) {
            System.out.println("Não é o servidor correto");
            return;
        }

        try {
            System.out.println("Tentando enviar mensagem de boas-vindas...");
            event.getGuild().getTextChannelById("1178024982887014470")
                 .sendMessage("Bem-vindo, " + event.getMember().getAsMention() + "!")
                 .queue();
            System.out.println("Mensagem enviada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem de boas-vindas: " + e.getMessage());
        }
    }
}