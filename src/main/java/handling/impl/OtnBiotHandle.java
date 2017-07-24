package handling.impl;

import components.keyboard.IKeyboard;
import database.dao.CategoryDao;
import database.dao.UserDao;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import util.Json;
import util.stepmapping.Step;


public class OtnBiotHandle extends AbstractHandle{

    private CategoryDao categoryDao = daoFactory.getCategoryDao();
    private UserDao userDao = daoFactory.getUserDao();

    @Step(value = "O_OtnBiot", commandText = "ОТН БиОТ")
    public void OtnBiot() throws Exception {

        IKeyboard kb = new IKeyboard();
        kb.next(1,1);
        kb.addButton("БиОТ", Json.set("step","Biot"));
        kb.addButton("ОТН", Json.set("step","Otn"));

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }








}
