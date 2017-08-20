package pro.nextbit.telegramconstructor;

import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClearMessage {

    private static Map<Long, List<Integer>> msgMap = new HashMap<>();
    private static Map<Long, List<Integer>> msgClearedLaterMap = new HashMap<>();

    public void clear(Message message) {
        set(message, msgMap);
    }

    public void clearLater(Message message){
        set(message, msgClearedLaterMap);
    }

    public List<Integer> get(long chatId) {
        List<Integer> list = new ArrayList<>();
        if (msgMap.containsKey(chatId)){
            list = msgMap.get(chatId);
            msgMap.remove(chatId);
        }
        return list;
    }

    public List<Integer> getAsLater(Message message) {
        if (msgClearedLaterMap.containsKey(message.getChatId())){
            List<Integer> list = msgClearedLaterMap.get(message.getChatId());
            if (list.contains(message.getMessageId())){
                return new ArrayList<>();
            }

            msgClearedLaterMap.remove(message.getChatId());
            return list;

        }
        return new ArrayList<>();
    }

    private void set(Message message, Map<Long, List<Integer>> map) {
        if (map.containsKey(message.getChatId())){
            List<Integer> list = map.get(message.getChatId());
            list.add(message.getMessageId());
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(message.getMessageId());
            map.put(message.getChatId(), list);
        }
    }

    public void remove(TelegramLongPollingBot bot, long chatId) {
        remove(bot, chatId, msgMap.get(chatId));
    }

    public void removeAsLater(TelegramLongPollingBot bot, long chatId) {
        remove(bot, chatId, msgClearedLaterMap.get(chatId));
    }

    private void remove(TelegramLongPollingBot bot, long chatId, List<Integer> list) {

        if (list != null) {
            for (int messageId : list) {
                try {
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setChatId(String.valueOf(chatId));
                    deleteMessage.setMessageId(messageId);
                    bot.deleteMessage(deleteMessage);
                } catch (Exception ignore) {}
            }
        }
    }
}
