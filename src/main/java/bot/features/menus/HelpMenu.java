package bot.features.menus;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.util.Arrays;

import bot.base.MenuResponse;

public class HelpMenu {
    private static MenuPage[] menuPages = {
        new HelpMenuPage("Comandos", "Crie comandos na pasta features > commands!", "#FF0000"),
        new HelpMenuPage("Botões", "Crie botões na pasta features > responders!", "#00FF00"),
        new HelpMenuPage("Eventos", "Crie eventos na pasta features > events!", "#0000FF")
    };

    public static MenuResponse MenuHelp(int page) {
        if (page < 0 || page >= menuPages.length) {
            throw new IllegalArgumentException("Invalid page number");
        }

        MenuPage menuPage = menuPages[page];

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(menuPage.getTitle())
            .setDescription(menuPage.getDescription())
            .setColor(Color.decode(menuPage.getColor()));

        Button nextButton = Button.primary("helpMenu/" + (page + 1), "Próxima Página");
        nextButton = nextButton.withDisabled(page == menuPages.length - 1);

        Button previousButton = Button.primary("helpMenu/" + (page - 1), "Página Anterior");
        previousButton = previousButton.withDisabled(page == 0);

        return new MenuResponse(
            embed.build(),
            Arrays.asList(previousButton, nextButton),
            true // Define a resposta como ephemeral
        );
    }

    public static class HelpMenuPage implements MenuPage {
        private String title;
        private String description;
        private String color;
    
        public HelpMenuPage(String title, String description, String color) {
            this.title = title;
            this.description = description;
            this.color = color;
        }
    
        @Override
        public String getTitle() {
            return title;
        }
    
        @Override
        public String getDescription() {
            return description;
        }
    
        @Override
        public String getColor() {
            return color;
        }
    }

    public interface MenuPage {
        String getTitle();
        String getDescription();
        String getColor();
    }
}
