package pro.nextbit.telegramconstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClearMessage {

    private static Map<Long, List<Integer>> messageIdMap = new HashMap<>();

    public static void set(long chatId, int messageId) {
        if (messageIdMap.containsKey(chatId)){
            List<Integer> list = messageIdMap.get(chatId);
            list.add(messageId);
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(messageId);
            messageIdMap.put(chatId, list);
        }
    }

    public static List<Integer> get(long chatId) {
        List<Integer> list = new ArrayList<>();
        if (messageIdMap.containsKey(chatId)){
            list = messageIdMap.get(chatId);
            messageIdMap.remove(chatId);
        }
        return list;
    }
}
