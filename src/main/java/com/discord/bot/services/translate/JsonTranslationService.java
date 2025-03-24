package com.discord.bot.services.translate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class JsonTranslationService {
    private final Map<String, Object> translations;

    public JsonTranslationService(String languageTag) throws IOException {
        String path = "src/main/resources/strings/" + languageTag + ".json";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        ObjectMapper mapper = new ObjectMapper();
        this.translations = mapper.readValue(content, Map.class);
    }

    public String get(String key) {
        return get(key, Collections.emptyMap());
    }

    public String get(String key, Map<String, String> replacements) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = translations;

        for (int i = 0; i < keys.length - 1; i++) {
            if (!current.containsKey(keys[i])) {
                return getDefaultError();
            }
            current = (Map<String, Object>) current.get(keys[i]);
        }

        if (!current.containsKey(keys[keys.length - 1])) {
            return getDefaultError();
        }

        String result = (String) current.get(keys[keys.length - 1]);

        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                result = result.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        return result;
    }

    private String getDefaultError() {
        return (String) ((Map<String, Object>) translations.getOrDefault("defaultErrors", Map.of()))
                .getOrDefault("unknown_error", "Erro desconhecido.");
    }
}
