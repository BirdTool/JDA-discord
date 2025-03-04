package bot.Services.Database;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;

public class DbManager {
    private static DB db;
    
    public static class UserData implements Serializable {
        private static final long serialVersionUID = 1L;
        private Map<String, Object> properties; // Mudado para Object para suportar mapas aninhados

        public UserData() {
            this.properties = new HashMap<>();
        }

        public void setProperty(String key, Object value) {
            properties.put(key, value);
        }

        public Object getProperty(String key) {
            return properties.get(key);
        }

        public void removeProperty(String key) {
            properties.remove(key);
        }

        public Map<String, Object> getAllProperties() {
            return properties;
        }
    }

    static {
        db = DBMaker.fileDB("database.db")
                    .fileMmapEnableIfSupported()
                    .make();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!db.isClosed()) {
                db.close();
            }
        }));
    }

    public static void addMap(String mapName) {
        db.hashMap(mapName, Serializer.STRING, Serializer.JAVA)
          .createOrOpen();
        db.commit();
    }

    public static void setValue(String mapName, String key, UserData value) {
        ConcurrentMap<String, UserData> map = db.hashMap(mapName, Serializer.STRING, Serializer.JAVA)
                                               .createOrOpen();
        map.put(key, value);
        db.commit();
    }

    public static void removeValue(String mapName, String key) {
        ConcurrentMap<String, UserData> map = db.hashMap(mapName, Serializer.STRING, Serializer.JAVA)
                                               .createOrOpen();
        map.remove(key);
        db.commit();
    }

    public static UserData getValue(String mapName, String key) {
        ConcurrentMap<String, UserData> map = db.hashMap(mapName, Serializer.STRING, Serializer.JAVA)
                                               .createOrOpen();
        return map.get(key);
    }

    public static void close() {
        if (!db.isClosed()) {
            db.close();
        }
    }
}