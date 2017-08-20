package handling.impl;

import database.DaoFactory;
import database.dao.UserDao;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import pro.nextbit.telegramconstructor.stepmapping.Step;

public class DefaultHandle extends AbstractHandle {

    private UserDao userDao = DaoFactory.getInstance().getUserDao();

    @Step("defaultStep")
    public void defaultStep() throws Exception {

//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText("...")
        );
    }
}
