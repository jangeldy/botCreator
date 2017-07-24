package handling.impl;

import components.keyboard.Keyboard;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import util.stepmapping.Step;

public class MainMenuHandle extends AbstractHandle {


    @Step(value = "M_main_menu", commandText = "Назад")
    public void mainMenu() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next(2,2,2,1);
        kb.addButton("✉ Рассылка");
        kb.addButton("⚒ Конструктив");
        kb.addButton("\uD83D\uDCC2 Команды");
        kb.addButton("Партнеры");
        kb.addButton("ОТН БиОТ");
        kb.addButton("Опрос");
        kb.addButton("\uD83D\uDCCD Информация");

        bot.sendMessage(new SendMessage()
                .setText(".")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate())
        );
    }
}
