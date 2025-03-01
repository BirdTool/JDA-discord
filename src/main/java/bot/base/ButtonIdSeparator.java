package bot.base;

public class ButtonIdSeparator {

    /**
     * Separa o ID do botão em duas partes com base no delimitador "/".
     *
     * @param buttonId O ID do botão (exemplo: "helpMenu/1").
     * @return Um array de strings com as partes separadas (exemplo: ["helpMenu", "1"]).
     * @throws IllegalArgumentException Se o ID do botão não contiver o delimitador "/".
     */
    public static String[] separate(String buttonId) {
        if (!buttonId.contains("/")) {
            throw new IllegalArgumentException("O ID do botão deve conter o delimitador '/'.");
        }
        return buttonId.split("/", 2); // Divide o ID em no máximo 2 partes
    }

    /**
     * Obtém uma parte específica do ID do botão.
     *
     * @param buttonId O ID do botão (exemplo: "helpMenu/1").
     * @param index    O índice da parte desejada (0 para o prefixo, 1 para o valor).
     * @return A parte do ID correspondente ao índice.
     * @throws IllegalArgumentException Se o ID do botão não contiver o delimitador "/".
     */
    public static String getPart(String buttonId, int index) {
        String[] parts = separate(buttonId);
        if (index < 0 || index >= parts.length) {
            throw new IllegalArgumentException("Índice inválido. O ID do botão contém apenas " + parts.length + " partes.");
        }
        return parts[index];
    }
}