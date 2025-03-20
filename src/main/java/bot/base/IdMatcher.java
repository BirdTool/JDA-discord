package bot.base;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IdMatcher {
    public static boolean matches(String pattern, String id) {
        // Se não houver asteriscos, é uma comparação direta
        if (!pattern.contains("*")) {
            return pattern.equals(id);
        }
        
        // Converte o padrão para uma expressão regular
        String regex = pattern
            .replace(".", "\\.")  // Escapa pontos
            .replace("?", "\\?")  // Escapa pontos de interrogação
            .replace("*", "([^/]+)"); // Substitui * por um grupo de captura
        
        return Pattern.compile(regex).matcher(id).matches();
    }

    public static Map<Integer, String> extractValues(String pattern, String id) {
        Map<Integer, String> values = new HashMap<>();
        
        // Se não houver asteriscos, não há valores para extrair
        if (!pattern.contains("*")) {
            return values;
        }
        
        // Divide o padrão e o ID em partes
        String[] patternParts = pattern.split("/");
        String[] idParts = id.split("/");
        
        // Se o número de partes for diferente, não há correspondência
        if (patternParts.length != idParts.length) {
            return values;
        }
        
        // Extrai os valores dinâmicos
        int valueIndex = 0;
        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].equals("*")) {
                values.put(valueIndex++, idParts[i]);
            }
        }
        
        return values;
    }
    public static String getValue(String pattern, String id, int index) {
        return extractValues(pattern, id).get(index);
    }
}
