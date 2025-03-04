package bot.Services.Database;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class TempDbManager {
    private static DB db;

    // Inicializa o banco de dados em memória uma única vez
    static {
        db = DBMaker.memoryDB()
                    .make();
        // Shutdown hook para fechar o DB ao encerrar a JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!db.isClosed()) {
                db.close();
            }
        }));
    }

    // Guardar valores sem expiração
    public static void setValue(String mapName, String key, String value) {
        ConcurrentMap<String, String> map = db.hashMap(mapName, org.mapdb.Serializer.STRING, org.mapdb.Serializer.STRING)
                                             .createOrOpen();
        map.put(key, value);
    }

    // Guardar valores com expiração automática
    public static void setValueWithTTL(String mapName, String key, String value, long ttl, TimeUnit unit) {
        ConcurrentMap<String, String> map = db.hashMap(mapName, org.mapdb.Serializer.STRING, org.mapdb.Serializer.STRING)
                                             .expireAfterCreate(ttl, unit) // Define o TTL para novos valores
                                             .createOrOpen();
        map.put(key, value);
    }

    // Excluir valores manualmente
    public static void removeValue(String mapName, String key) {
        ConcurrentMap<String, String> map = db.hashMap(mapName, org.mapdb.Serializer.STRING, org.mapdb.Serializer.STRING)
                                             .createOrOpen();
        map.remove(key);
    }

    // Pegar valores
    public static String getValue(String mapName, String key) {
        ConcurrentMap<String, String> map = db.hashMap(mapName, org.mapdb.Serializer.STRING, org.mapdb.Serializer.STRING)
                                             .createOrOpen();
        return map.get(key); // Retorna String ou null se não existir ou tiver expirado
    }

    // Método para fechar manualmente (opcional)
    public static void close() {
        if (!db.isClosed()) {
            db.close();
        }
    }
}