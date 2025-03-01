package bot.base;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class MenuResponse {
    private final MessageEmbed embed;
    private final List<Button> buttons;
    private final boolean ephemeral;

    public MenuResponse(MessageEmbed embed, List<Button> buttons, boolean ephemeral) {
        this.embed = embed;
        this.buttons = buttons;
        this.ephemeral = ephemeral;
    }

    public MessageEmbed getEmbed() {
        return embed;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }
}