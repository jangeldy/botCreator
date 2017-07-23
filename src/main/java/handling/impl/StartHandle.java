package handling.impl;

import components.keyboard.IKeyboard;
import database.dao.CategoryDao;
import database.dao.UserDao;
import database.entity.CategoryEntity;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import util.Json;
import util.stepmapping.Step;


public class StartHandle extends AbstractHandle {

    private CategoryDao positionDao = daoFactory.getPositionDao();
    private UserDao usersDao = daoFactory.getUsersDao();


    @Step(value = "S_start", commandText = "/start")
    public void start() throws Exception {

        if (!usersDao.checkUser(chatId)){

            IKeyboard kb = new IKeyboard();
            kb.next();

            for (CategoryEntity p : positionDao.getList()){
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
