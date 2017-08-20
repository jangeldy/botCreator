package handling.impl;

import database.DaoFactory;
import database.dao.UserDao;
import handling.AbstractHandle;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import pro.nextbit.telegramconstructor.components.datepicker.DatePicker;
import pro.nextbit.telegramconstructor.stepmapping.Step;

import java.util.Date;

public class DefaultHandle extends AbstractHandle {

    private UserDao userDao = DaoFactory.getInstance().getUserDao();

    @Step("defaultStep")
    public void defaultStep() throws Exception {

//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        DatePicker datePicker = new DatePicker(queryData, step);
        DateTime date = datePicker.getDate(bot, "Выберите дату рождения", message);
        String name = dataRequest("Введите имя");

        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setText(name + date.toString())
                        .setChatId(chatId)
                )
        );
    }

}
