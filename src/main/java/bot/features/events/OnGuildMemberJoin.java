package bot.features.events;

import bot.base.RegisterEvent;
import bot.base.EventHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

@RegisterEvent
public class OnGuildMemberJoin extends ListenerAdapter implements EventHandler {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println("Novo membro: " + event.getMember().getUser().getName());
        try {
            event.getGuild().getDefaultChannel().asTextChannel()
                 .sendMessage("Bem-vindo ao servidor, " + event.getMember().getAsMention() + "!")
                 .queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Class<?> getEventType() {
        return GuildMemberJoinEvent.class;
    }
}
