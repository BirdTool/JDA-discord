package bot.features.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import bot.base.Command;
import bot.base.EasyEmbedUtil;
import bot.base.RegisterCommand;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@RegisterCommand
public class Counter implements Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue(); // Evita timeout
        
        new Thread(() -> { // Executa a contagem em uma thread separada
            int number = 0;
            long startTime = System.currentTimeMillis();
            int seconds = event.getOption("número") != null ? event.getOption("número").getAsInt() : 1;
            long timeLimit = seconds * 1000;
            
            // Conta o máximo possível em 1 segundo
            while (System.currentTimeMillis() - startTime < timeLimit) {
                number++;
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            // 🔹 Formata o número com separadores de milhar
            String formattedNumber = formatWithThousandsSeparator(number);
            // 🔹 Abrevia o número (ex: 9.95M)
            String abbreviatedNumber = abbreviateNumber(number);

            // 🔹 Gera a mensagem formatada
            String description = "Número: **" + formattedNumber + "** (" + abbreviatedNumber + ")\n"
                               + "Tempo: **" + elapsedTime + "**ms";

            event.getHook().editOriginalEmbeds(EasyEmbedUtil.createEmbed("Contagem Finalizada", description, "#7ebf38")).queue();
        }).start();
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("contar", "Conta até o máximo possível em determinado segundo")
            .addOptions(new net.dv8tion.jda.api.interactions.commands.build.OptionData(OptionType.INTEGER, "número", "Segundos para terminar a contagem", false)
                .setMinValue(1)
                .setMaxValue(15));
    }

    // Método para formatar com separadores de milhar (ex: 9.496.553)
    private String formatWithThousandsSeparator(int number) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("pt", "BR"));
        return formatter.format(number);
    }

    // Método para abreviar números grandes (ex: 9.95M)
    private String abbreviateNumber(int number) {
        if (number < 1000) return String.valueOf(number);
        double num = number;
        String[] suffixes = {"", "K", "M", "B", "T"}; // Mil, Milhão, Bilhão, Trilhão
        int index = 0;
        while (num >= 1000 && index < suffixes.length - 1) {
            num /= 1000;
            index++;
        }
        return new DecimalFormat("#.##").format(num) + suffixes[index];
    }
}