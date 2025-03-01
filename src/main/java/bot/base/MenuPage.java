package bot.base;

public class MenuPage {
    private final String title;
    private final String description;
    private final String color;

    public MenuPage(String title, String description, String color) {
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}