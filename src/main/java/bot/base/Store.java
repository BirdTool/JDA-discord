package bot.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Store<T> {

    private final Map<String, T> storage = new HashMap<>();
    private final Map<String, Timer> timers = new HashMap<>();

    /**
     * Armazena um valor associado ao ID do usuário.
     * Se um tempo de expiração for fornecido, o valor será removido após esse tempo.
     *
     * @param userId  O ID do usuário.
     * @param value   O valor a ser armazenado.
     * @param time    O tempo em milissegundos até o valor ser removido (opcional).
     */
    public void set(String userId, T value, Long time) {
        storage.put(userId, value);

        if (time != null && time > 0) {
            // Se um tempo for fornecido, agenda a remoção do valor após o tempo especificado
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    storage.remove(userId);
                    timers.remove(userId);
                }
            }, time);
            timers.put(userId, timer);
        }
    }

    /**
     * Armazena um valor associado ao ID do usuário sem tempo de expiração.
     *
     * @param userId  O ID do usuário.
     * @param value   O valor a ser armazenado.
     */
    public void set(String userId, T value) {
        set(userId, value, null);
    }

    /**
     * Obtém o valor associado ao ID do usuário.
     *
     * @param userId  O ID do usuário.
     * @return O valor armazenado ou null se não existir.
     */
    public T get(String userId) {
        return storage.get(userId);
    }

    /**
     * Remove o valor associado ao ID do usuário.
     *
     * @param userId  O ID do usuário.
     */
    public void remove(String userId) {
        storage.remove(userId);
        if (timers.containsKey(userId)) {
            timers.get(userId).cancel();
            timers.remove(userId);
        }
    }

    /**
     * Verifica se um valor está armazenado para o ID do usuário.
     *
     * @param userId  O ID do usuário.
     * @return true se o valor existir, false caso contrário.
     */
    public boolean has(String userId) {
        return storage.containsKey(userId);
    }

    /**
     * Limpa todos os valores armazenados.
     */
    public void clear() {
        storage.clear();
        timers.values().forEach(Timer::cancel);
        timers.clear();
    }
}