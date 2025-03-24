package com.discord.bot.features.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.discord.bot.base.utils.Colors;
import com.discord.bot.services.translate.JsonTranslationService;

public class Counter {
    /**
     * Manipulador para o subcomando "world"
     * @param event O evento de interação do slash command
     * @throws IOException 
     */
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue(); // Evita timeout
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int number = 0;
                    long startTime = System.currentTimeMillis();
                    int seconds = event.getOption("maximum") != null ? event.getOption("maximum").getAsInt() : 1;
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

                    DiscordLocale userLocale = event.getUserLocale();
            
                    // Mapeia o DiscordLocale para o código de idioma usado nos arquivos de tradução
                    String locale;
                    if (userLocale == DiscordLocale.PORTUGUESE_BRAZILIAN) {
                        locale = "pt-br";
                    } else if (userLocale == DiscordLocale.SPANISH) {
                        locale = "es-es";
                    } else {
                        locale = "en-us";
                    }
                    
                    // Carrega o serviço de tradução com o idioma correto
                    JsonTranslationService translation = new JsonTranslationService(locale);

                    Map<String, String> replacements = new HashMap<>();
                    replacements.put("formattedNumber", formattedNumber);
                    replacements.put("abbreviatedNumber", abbreviatedNumber);
                    replacements.put("elapsedTime", String.valueOf(elapsedTime));

                    // 🔹 Gera a mensagem formatada
                    String title = translation.get("counter.response.title", replacements);
                    String description = translation.get("counter.response.description", replacements);

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(title);
                    embed.setDescription(description);
                    embed.setColor(Integer.decode(Colors.success));

                    event.getHook().editOriginalEmbeds(embed.build()).queue();
                } catch (IOException e) {
                    event.getHook().editOriginal("An error occurred while processing the command.").queue();
                }
            }        });
        thread.start();
    }

    public SlashCommandData getCommandData() throws IOException {
        JsonTranslationService translation_en_us = new JsonTranslationService("en-us");
        JsonTranslationService translation_pt_br = new JsonTranslationService("pt-br");
        JsonTranslationService translation_es_es = new JsonTranslationService("es-es");
    
        // Comando principal
        String name_en_us = translation_en_us.get("counter.name");
        // Fallback to English if translation is missing or invalid
        String name_pt_br = translation_pt_br.get("counter.name");
        if (name_pt_br == null || name_pt_br.length() > 32) {
            name_pt_br = name_en_us;
        }
        String name_es_es = translation_es_es.get("counter.name");
        if (name_es_es == null || name_es_es.length() > 32) {
            name_es_es = name_en_us;
        }
    
        String description_en_us = translation_en_us.get("counter.description");
        String description_pt_br = translation_pt_br.get("counter.description");
        String description_es_es = translation_es_es.get("counter.description");
    
        // Opções
        String optionName_en_us = translation_en_us.get("counter.options.max.name");
        String optionName_pt_br = translation_pt_br.get("counter.options.max.name");
        String optionName_es_es = translation_es_es.get("counter.options.max.name");
    
        String optionDesc_en_us = translation_en_us.get("counter.options.max.description");
        String optionDesc_pt_br = translation_pt_br.get("counter.options.max.description");
        String optionDesc_es_es = translation_es_es.get("counter.options.max.description");
    
        // Ensure all command names are valid
        if (name_en_us == null || name_en_us.length() > 32) {
            name_en_us = "counter"; // Default fallback
        }
    
        return Commands.slash(name_en_us, description_en_us)
            .setNameLocalizations(Map.of(
                DiscordLocale.PORTUGUESE_BRAZILIAN, name_pt_br,
                DiscordLocale.SPANISH, name_es_es
            ))
            .setDescriptionLocalizations(Map.of(
                DiscordLocale.PORTUGUESE_BRAZILIAN, description_pt_br,
                DiscordLocale.SPANISH, description_es_es
            ))
            .addOptions(new net.dv8tion.jda.api.interactions.commands.build.OptionData(
                OptionType.INTEGER, 
                optionName_en_us, 
                optionDesc_en_us, 
                false
            )
            .setNameLocalizations(Map.of(
                DiscordLocale.PORTUGUESE_BRAZILIAN, optionName_pt_br,
                DiscordLocale.SPANISH, optionName_es_es
            ))
            .setDescriptionLocalizations(Map.of(
                DiscordLocale.PORTUGUESE_BRAZILIAN, optionDesc_pt_br,
                DiscordLocale.SPANISH, optionDesc_es_es
            ))
            .setMinValue(1)
            .setMaxValue(15));
    }
    

    public String getPrincipalCommandName() {
        return "counter";
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