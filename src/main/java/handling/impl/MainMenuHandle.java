package handling.impl;

import components.keyboard.Keyboard;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import util.stepmapping.Step;

public class MainMenuHandle extends AbstractHandle {


    @Step("Назад")
    public void M_main_menu() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next(2,2,2,1);
        kb.add("✉ Рассылка");
        kb.add("⚒ Конструктив");
        kb.add("\uD83D\uDCC2 Команды");
        kb.add("Партнеры");
        kb.add("ОТН БиОТ");
        kb.add("Опрос");
        kb.add("\uD83D\uDCCD Информация");

        bot.sendMessage(new SendMessage()
                .setText(".")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate())
        );
    }
}
