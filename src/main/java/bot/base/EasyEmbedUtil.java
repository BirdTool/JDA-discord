package bot.base;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;

public class EasyEmbedUtil {

    public static MessageEmbed createEmbed(String title, String description, String color) {
        // Se ambos title e description forem nulos ou vazios, lança exceção
        if ((title == null || title.isBlank()) && (description == null || description.isBlank())) {
            throw new IllegalArgumentException("Pelo menos um título ou descrição deve ser fornecido!");
        }

        EmbedBuilder embed = new EmbedBuilder();

        if (title != null && !title.isBlank()) {
            embed.setTitle(title);
        }

        if (description != null && !description.isBlank()) {
            embed.setDescription(description);
        }

        if (color != null && !color.isBlank()) {
            try {
                embed.setColor(Color.decode(color));
            } catch (NumberFormatException e) {
                embed.setColor(Color.GRAY); // Se a cor for inválida, usa um fallback
            }
        }

        return embed.build();
    }
}
