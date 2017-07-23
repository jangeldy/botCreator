package handling.impl;

import components.keyboard.IKeyboard;
import database.dao.CategoryDao;
import database.dao.UserDao;
import database.entity.CategoryEntity;
import database.entity.UserEntity;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import util.Json;
import util.StepParam;
import util.stepmapping.Step;

import java.util.List;


public class StartHandle extends AbstractHandle {

    private CategoryDao categoryDao = daoFactory.getCategoryDao();
    private UserDao usersDao = daoFactory.getUserDao();


    @Step(value = "S_start", commandText = "/start")
    public void start() throws Exception {

        if (!usersDao.checkUser(chatId)){

            setMessageToClear(
                    bot.sendMessage(new SendMessage()
                            .setText("Введите ваше имя")
                            .setChatId(chatId)
                    )
            );

            step = "S_start2";

        } else {
            redirect("M_main_menu");
        }

    }


    @Step("S_start2")
    public void start2() throws Exception {

        setMessageToClear(
                bot.sendMessage(new SendMessage()
                        .setText("Введите вашу фамилию")
                        .setChatId(chatId)
                )
        );

        StepParam param = new StepParam(chatId, "S_registr");
        param.set("newUserName", inputText);
        step = "S_start3";

    }


    @Step("S_start3")
    public void start3() throws Exception {

        int category = 0;
        if (queryData.containsKey("category")){

            category = queryData.getInt("category");


            if (queryData.get("parent") != null
                    && queryData.getInt("parent") == 6){

                StepParam param = new StepParam(chatId, "S_registr");
                param.set("category", category);
                setMessageToClear(
                        bot.sendMessage(new SendMessage()
                                .setText("Введите наименование компаний")
                                .setChatId(chatId)
                        )
                );
                step = "S_start4";
                return;

            } else if (category != 6){

                StepParam param = new StepParam(chatId, "S_registr");
                param.set("category", category);
                redirect("S_start5");
                return;
            }

        } else {

            StepParam param = new StepParam(chatId, "S_registr");
            param.set("newUserSurname", inputText);

        }

        IKeyboard kb = new IKeyboard();
        kb.next();

        List<CategoryEntity> categoryList = categoryDao.getListByParent(category);
        for (CategoryEntity ce : categoryList) {
            kb.addButton(
                    ce.getName(),
                    Json.set("step", "S_start3")
                            .set("category", ce.getId())
                            .set("parent", ce.getParent())
            );
        }

        setMessageToClear(
                bot.sendMessage(new SendMessage()
                        .setText("Выберите к какой категорий относитесь")
                        .setChatId(chatId)
                        .setReplyMarkup(kb.generate())
                )
        );

    }


    @Step("S_start4")
    public void start4() throws Exception {

        StepParam param = new StepParam(chatId, "S_registr");
        param.set("companyName", inputText);
        redirect("S_start5");
    }


    @Step("S_start5")
    public void start5() throws Exception {
        setMessageToClear(
                bot.sendMessage(new SendMessage()
                        .setText("Введите вашу должность")
                        .setChatId(chatId)
                )
        );
        step = "S_registr";
    }


    @Step("S_registr")
    public void registr() throws Exception {
        StepParam param = new StepParam(chatId, "S_registr");

        String name = param.getString("newUserName");
        String surname = param.getString("newUserSurname");
        int idCategory = param.getInt("category");
        String position = inputText;
        String companyName = null;

        if (param.containsKey("companyName")){
            companyName = param.getString("companyName");
        }

        UserEntity user = new UserEntity();
        user.setChatId(chatId);
        user.setIdCatogory(idCategory);
        user.setUserName(name);
        user.setUserSurname(surname);
        user.setPosition(position);
        user.setCompanyName(companyName);
        usersDao.insert(user);

        redirect("M_main_menu");
    }

}
