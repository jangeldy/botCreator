package handling.impl;

import components.keyboard.Keyboard;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.Json;


public class DefaultHandle extends AbstractHandle {

    @Override
    public void handling() throws TelegramApiException {

        Keyboard kb = new Keyboard(true, 1,2,2);
        kb.addButton("button1", Json.set("aaa","aaa"));
        kb.addButton("button2", Json.set("aaa","aaa"));
        kb.addButton("button3", Json.set("aaa","aaa"));
        kb.addButton("button4", Json.set("aaa","aaa"));
        kb.addButton("button5", Json.set("aaa","aaa"));

        bot.sendMessage(new SendMessage()
                .setText("gfgfgfgf")
                .setChatId(chatId)
                .setReplyMarkup(kb.get())
        );
    }
}
