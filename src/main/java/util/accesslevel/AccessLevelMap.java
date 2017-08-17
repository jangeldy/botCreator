package util.accesslevel;

import java.util.HashMap;
import java.util.Map;

public class AccessLevelMap {

    private static Map<Long, AccessLevel> accessLevelMap = new HashMap<>();

    public static AccessLevel get(long chatId) {
        return accessLevelMap.getOrDefault(chatId, AccessLevel.READ);
    }

    public static void set(long chatId, AccessLevel accessLevel) {
        accessLevelMap.put(chatId, accessLevel);
    }
}
