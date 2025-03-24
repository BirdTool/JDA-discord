package com.discord.bot.base.utils;

import com.discord.bot.services.translate.JsonTranslationService;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.DiscordLocale;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandTranslationHelper {
    private static final String[] SUPPORTED_LOCALES = {"en-us", "pt-br", "es-es"};
    private static final Map<String, DiscordLocale> LOCALE_MAPPING = Map.of(
        "en-us", DiscordLocale.ENGLISH_US,
        "pt-br", DiscordLocale.PORTUGUESE_BRAZILIAN,
        "es-es", DiscordLocale.SPANISH
    );
    private static final String DEFAULT_LOCALE = "en-us";

    private final Map<String, JsonTranslationService> translators = new HashMap<>();

    public CommandTranslationHelper() throws IOException {
        for (String locale : SUPPORTED_LOCALES) {
            translators.put(locale, new JsonTranslationService(locale));
        }
    }

    /**
     * Constrói um SlashCommandData com traduções automáticas
     * @param commandKeyPrefix Prefixo da chave do comando (ex: "counter")
     * @param defaultName Nome padrão caso a tradução falhe
     * @return SlashCommandData configurado com traduções
     */
    public SlashCommandData buildCommand(String commandKeyPrefix, String defaultName) {
        String baseKey = commandKeyPrefix + ".name";
        String descKey = commandKeyPrefix + ".description";

        String name = getValidatedTranslation(baseKey, defaultName);
        String description = getTranslation(descKey);

        SlashCommandData command = Commands.slash(name, description);
        addNameLocalizations(command, baseKey);
        addDescriptionLocalizations(command, descKey);

        return command;
    }

    /**
     * Adiciona uma opção ao comando ou subcomando com traduções
     * @param target Comando ou subcomando ao qual adicionar a opção
     * @param optionKeyPrefix Prefixo da chave da opção (ex: "counter.options.max")
     * @param type Tipo da opção
     * @param required Se a opção é obrigatória
     * @return OptionData configurado com traduções
     */
    public OptionData addOption(Object target, String optionKeyPrefix, OptionType type, boolean required) {
        String nameKey = optionKeyPrefix + ".name";
        String descKey = optionKeyPrefix + ".description";

        String name = getValidatedTranslation(nameKey, "option"); // Fallback genérico
        String description = getTranslation(descKey);

        OptionData option = new OptionData(type, name, description, required);
        addNameLocalizations(option, nameKey);
        addDescriptionLocalizations(option, descKey);

        if (target instanceof SlashCommandData) {
            ((SlashCommandData) target).addOptions(option);
        } else if (target instanceof SubcommandData) {
            ((SubcommandData) target).addOptions(option);
        }

        return option;
    }

    /**
     * Adiciona um subcomando ao comando com traduções
     * @param command Comando ao qual adicionar o subcomando
     * @param subcommandKeyPrefix Prefixo da chave do subcomando (ex: "hello.subCommands.world")
     * @return SubcommandData configurado com traduções
     */
    public SubcommandData addSubcommand(SlashCommandData command, String subcommandKeyPrefix) {
        String nameKey = subcommandKeyPrefix + ".name";
        String descKey = subcommandKeyPrefix + ".description";

        String name = getValidatedTranslation(nameKey, "subcommand"); // Fallback genérico
        String description = getTranslation(descKey);

        SubcommandData subcommand = new SubcommandData(name, description);
        addNameLocalizations(subcommand, nameKey);
        addDescriptionLocalizations(subcommand, descKey);

        command.addSubcommands(subcommand);
        return subcommand;
    }

    /**
     * Adiciona um grupo de subcomandos ao comando com traduções
     * @param command Comando ao qual adicionar o grupo
     * @param groupKeyPrefix Prefixo da chave do grupo (ex: "hello.subCommands")
     * @param groupName Nome do grupo (ex: "greet")
     * @return SubcommandGroupData configurado com traduções
     */
    public SubcommandGroupData addSubcommandGroup(SlashCommandData command, String groupKeyPrefix, String groupName) {
        String nameKey = groupKeyPrefix + "." + groupName + ".name";
        String descKey = groupKeyPrefix + "." + groupName + ".description";

        String name = getValidatedTranslation(nameKey, groupName);
        String description = getTranslation(descKey);

        SubcommandGroupData group = new SubcommandGroupData(name, description);
        addNameLocalizations(group, nameKey);
        addDescriptionLocalizations(group, descKey);

        command.addSubcommandGroups(group);
        return group;
    }

    /**
     * Adiciona um subcomando a um grupo com traduções
     * @param group Grupo ao qual adicionar o subcomando
     * @param subcommandKeyPrefix Prefixo da chave do subcomando (ex: "hello.subCommands.greet.world")
     * @return SubcommandData configurado com traduções
     */
    public SubcommandData addSubcommandToGroup(SubcommandGroupData group, String subcommandKeyPrefix) {
        String nameKey = subcommandKeyPrefix + ".name";
        String descKey = subcommandKeyPrefix + ".description";

        String name = getValidatedTranslation(nameKey, "subcommand"); // Fallback genérico
        String description = getTranslation(descKey);

        SubcommandData subcommand = new SubcommandData(name, description);
        addNameLocalizations(subcommand, nameKey);
        addDescriptionLocalizations(subcommand, descKey);

        group.addSubcommands(subcommand);
        return subcommand;
    }

    private String getValidatedTranslation(String key, String defaultValue) {
        String value = getTranslation(key);
        if (value == null || value.length() > 32) {
            return defaultValue;
        }
        return value;
    }

    private String getTranslation(String key) {
        return translators.get(DEFAULT_LOCALE).get(key);
    }

    private void addNameLocalizations(Object target, String key) {
        Map<DiscordLocale, String> localizations = new HashMap<>();
        for (String locale : SUPPORTED_LOCALES) {
            if (!locale.equals(DEFAULT_LOCALE)) {
                String value = translators.get(locale).get(key);
                if (value != null && value.length() <= 32) {
                    localizations.put(LOCALE_MAPPING.get(locale), value);
                }
            }
        }
        if (target instanceof SlashCommandData) {
            ((SlashCommandData) target).setNameLocalizations(localizations);
        } else if (target instanceof OptionData) {
            ((OptionData) target).setNameLocalizations(localizations);
        } else if (target instanceof SubcommandData) {
            ((SubcommandData) target).setNameLocalizations(localizations);
        } else if (target instanceof SubcommandGroupData) {
            ((SubcommandGroupData) target).setNameLocalizations(localizations);
        }
    }

    private void addDescriptionLocalizations(Object target, String key) {
        Map<DiscordLocale, String> localizations = new HashMap<>();
        for (String locale : SUPPORTED_LOCALES) {
            if (!locale.equals(DEFAULT_LOCALE)) {
                String value = translators.get(locale).get(key);
                if (value != null) {
                    localizations.put(LOCALE_MAPPING.get(locale), value);
                }
            }
        }
        if (target instanceof SlashCommandData) {
            ((SlashCommandData) target).setDescriptionLocalizations(localizations);
        } else if (target instanceof OptionData) {
            ((OptionData) target).setDescriptionLocalizations(localizations);
        } else if (target instanceof SubcommandData) {
            ((SubcommandData) target).setDescriptionLocalizations(localizations);
        } else if (target instanceof SubcommandGroupData) {
            ((SubcommandGroupData) target).setDescriptionLocalizations(localizations);
        }
    }
}