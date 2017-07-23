package handling.impl;

import components.keyboard.IKeyboard;
import database.dao.PositionDao;
import database.dao.UsersDao;
import database.entity.PositionEntity;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import util.GlobalParam;
import util.Json;
import util.stepmapping.Step;

import java.util.List;

public class StartHandle extends AbstractHandle {

    private PositionDao positionDao = daoFactory.getPositionDao();
    private UsersDao usersDao = daoFactory.getUsersDao();


    @Step(value = "S_start", commandText = "/start")
    public void start() throws Exception {

        log.info("dsfsdfsd");
        if (!usersDao.checkUser(chatId)){

            IKeyboard kb = new IKeyboard();
            kb.next();

            for (PositionEntity p : positionDao.getList()){
                kb.addButton(
                        p.getName(),
                        Json.set("pos_id", p.getId())
                );
            }

            setMessageToClear(
                    bot.sendMessage(new SendMessage()
                            .setChatId(chatId)
                            .setText("К какой позиций вы относитесь?")
                            .setReplyMarkup(kb.generate())
                    )
            );
        }

    }
}
