package handling;

import database.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import pro.nextbit.telegramconstructor.handle.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import pro.nextbit.telegramconstructor.stepmapping.Step;

public class DefaultHandle extends AbstractHandle {

    @Autowired
    private UserDao userDao;

    @Step("defaultStep")
    public void defaultStep() throws Exception {

//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText("...")
        );
    }
}
