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

    @Step("ОТН БиОТ")
    public void O_OtnBiot() throws Exception {

        IKeyboard kb = new IKeyboard();
        kb.next(1,1);
        kb.add("БиОТ", Json.set("step","Biot"));
        kb.add("ОТН", Json.set("step","Otn"));

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }








}
