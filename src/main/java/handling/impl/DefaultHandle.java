package handling.impl;

import database.dao.UserDao;
import handling.AbstractHandle;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import pro.nextbit.telegramconstructor.components.datepicker.FullDatePicker;
import pro.nextbit.telegramconstructor.database.DataRec;
import pro.nextbit.telegramconstructor.stepmapping.Step;


public class DefaultHandle extends AbstractHandle {

    private UserDao userDao = daoFactory.getUserDao();

    @Step("defaultStep")
    public void defaultStep() throws Exception {

        FullDatePicker tm = new FullDatePicker(queryData, step);
        DateTime dateTime = tm.getDate(bot, "Выберите дату от", message);

        FullDatePicker tm2 = new FullDatePicker(queryData, step);
        DateTime dateTime2 = tm2.getDate(bot, "Выберите дату до", message);

        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setText("от" +dateTime.toString() + " " + tm.getWeek())
                        .setChatId(chatId)
                )
        );
        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setText("до" + dateTime2.toString() + " " + tm2.getWeek())
                        .setChatId(chatId)
                )
        );
    }

}
