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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendingHandle extends AbstractHandle {

    private CategoryDao categoryDao = daoFactory.getCategoryDao();
    private UserDao userDao = daoFactory.getUserDao();

    private Map<Integer, Boolean> categoryCheckBox;
    private Map<Long, Boolean> userCheckBox;

    private long chatidForAnswer;
    private long UserchatidForAnswer;
    private long chatidForPartners;

    @Step("✉ Рассылка")
    public void S_sending() throws Exception {

        StepParam param = new StepParam(chatId, "S_sendMsg");
        param.set("operation", "Рассылка");
        categoryCheckBox = new HashMap<>();
        List<CategoryEntity> categoryList = categoryDao.getListForSending(0);
        sendCategoryList(categoryList);
        step = "S_sendMsg";

    }


    @Step("\uD83D\uDCC2 Команды")
    public void S_sendingCommand() throws Exception {

        StepParam param = new StepParam(chatId, "S_sendMsg");
        param.set("operation", "Команды");
        categoryCheckBox = new HashMap<>();
        CategoryEntity category = categoryDao.get(1);
        CategoryEntity category2 = categoryDao.get(2);
        CategoryEntity category3 = categoryDao.get(3);
        List<CategoryEntity> categoryList = new ArrayList<>();
        categoryList.add(category);
        categoryList.add(category2);
        categoryList.add(category3);
        sendCategoryList(categoryList);
        step = "S_sendMsg";

    }


    @Step("Опрос")
    public void S_sendingInter() throws Exception {

        StepParam param = new StepParam(chatId, "S_sendMsg");
        param.set("operation", "Опрос");
        categoryCheckBox = new HashMap<>();
        List<CategoryEntity> categoryList = categoryDao.getListForSending(0);
        sendCategoryList(categoryList);
        step = "S_sendMsg";

    }


    @Step
    public void S_sendMsg() throws Exception {
        StepParam param = new StepParam(chatId, "S_sendMsg");
        String operation = param.getString("operation");
        if (param.containsKey("messageText")) {
            String messageText = param.getString("messageText");

        } else {
            param.set("messageText", inputText);

        }
        if (param.containsKey("isUser")) {
            boolean isUser = param.getBoolean("isUser");
            List<UserEntity> userList = new ArrayList<>();


            if (isUser) {

                for (Map.Entry<Long, Boolean> entry : userCheckBox.entrySet()) {
                    if (entry.getValue()) {
                        userList.add(userDao.getByChatId(entry.getKey()));
                    }
                }

            } else {

                for (Map.Entry<Integer, Boolean> entry : categoryCheckBox.entrySet()) {
                    if (entry.getValue()) {
                        userList.addAll(userDao.getListByCategoryRecusiv(entry.getKey()));
                    }
                }
            }
        }


        switch (operation) {
            case "Рассылка": {

                if (categoryCheckBox != null) {

                    for (Map.Entry<Integer, Boolean> entry : categoryCheckBox.entrySet()) {
                        if (entry.getValue()) {
                            for (UserEntity rec : userDao.getListByCategory(entry.getKey())) {
                                UserEntity mydata = userDao.getByChatId(chatId);
                                clearMessage(
                                        bot.sendMessage(new SendMessage()
                                                .setChatId(rec.getChatId())
                                                .setText("<b>\uD83D\uDD0A Расслка от " + mydata.getUserName() + " " +
                                                        mydata.getUserSurname() + " </b>\nДолжность: " +
                                                        mydata.getPosition() + "\nТекст: " + param.getString("messageText"))
                                                .enableHtml(true)
                                        )
                                );
                            }

                        }
                    }
                }
                if (userCheckBox != null) {

                    for (Map.Entry<Long, Boolean> entry : userCheckBox.entrySet()) {
                        if (entry.getValue()) {

                            UserEntity user = userDao.getByChatId(entry.getKey());
                            clearMessage(
                                    bot.sendMessage(new SendMessage()
                                            .setChatId(user.getChatId())
                                            .setText("<b>\uD83D\uDD0A Расслка от " + userDao.getByChatId(chatId).getUserName() + " " +
                                                    userDao.getByChatId(chatId).getUserSurname() + " </b>\nТекст: " + param.getString("messageText"))
                                            .enableHtml(true)
                                    )
                            );
                        }
                    }

                }


            }
            break;
            case "Команды": {


                if (categoryCheckBox != null) {


                    for (Map.Entry<Integer, Boolean> entry : categoryCheckBox.entrySet()) {
                        if (entry.getValue()) {

                            for (UserEntity rec : userDao.getListByCategory(entry.getKey())) {
                                IKeyboard kb = new IKeyboard();
                                kb.next(2);
                                UserEntity mydata = userDao.getByChatId(chatId);
                                kb.addButton("Ответить", Json.set("step", "S_answerEmployee").set("chatid", chatId));
                                kb.addButton("Пропустить", Json.set("step", "S_answerEmployee").set("chatid", chatId));
                                bot.sendMessage(new SendMessage()
                                        .setChatId(rec.getChatId())
                                        .setText("<b>✉️ Сообщения от " + mydata.getUserName() + " " +
                                                mydata.getUserSurname() + " </b>\nДолжность: " +
                                                mydata.getPosition() + "\nТекст: " + param.getString("messageText"))
                                        .enableHtml(true)
                                );

                                clearMessage(
                                        bot.sendMessage(new SendMessage()
                                                .setChatId(rec.getChatId())
                                                .setText("⬇️")
                                                .setReplyMarkup(kb.generate())
                                                .enableHtml(true)
                                        )
                                );

                            }
                        }

                    }
                }


                if (userCheckBox != null) {

                    for (Map.Entry<Long, Boolean> entry : userCheckBox.entrySet()) {
                        if (entry.getValue()) {
                            UserEntity mydata = userDao.getByChatId(chatId);
                            UserEntity user = userDao.getByChatId(entry.getKey());
                            IKeyboard kb = new IKeyboard();
                            kb.next(2);
                            kb.addButton("Ответить", Json.set("step", "S_answerEmployee").set("chatid", chatId));
                            kb.addButton("Пропустить", Json.set("step", "").set("chatid", chatId));


                            clearMessage(
                                    bot.sendMessage(new SendMessage()
                                            .setChatId(user.getChatId())
                                            .setText("<b>✉️ Сообщения от " + mydata.getUserName() + " " +
                                                    mydata.getUserSurname() + " </b>\nДолжность: " +
                                                    mydata.getPosition() + "\nТекст: " + param.getString("messageText"))
                                            .setReplyMarkup(kb.generate())
                                            .enableHtml(true)
                                    )
                            );

                        }

                    }
                }

            }


            break;
            case "Опрос": {

                if (categoryCheckBox != null) {

                    for (Map.Entry<Integer, Boolean> entry : categoryCheckBox.entrySet()) {
                        if (entry.getValue()) {
                            for (UserEntity rec : userDao.getListByCategory(entry.getKey())) {
                                IKeyboard kb = new IKeyboard();
                                kb.next(2);
                                kb.addButton("✅", Json.set("step", "S_answerEmployee").set("chatid", chatId));
                                kb.addButton("❎", Json.set("step", "").set("chatid", chatId));
                                UserEntity mydata = userDao.getByChatId(chatId);
                                clearMessage(
                                        bot.sendMessage(new SendMessage()
                                                .setChatId(rec.getChatId())
                                                .setText("<b>\uD83D\uDCE3 Опрос от " + mydata.getUserName() + " " +
                                                        mydata.getUserSurname() + " </b>\nДолжность: " +
                                                        mydata.getPosition() + "\nТекст: " + param.getString("messageText"))
                                                .setReplyMarkup(kb.generate())
                                                .enableHtml(true)
                                        )
                                );
                            }

                        }
                    }
                }
                if (userCheckBox != null) {

                    IKeyboard kb = new IKeyboard();

                    for (Map.Entry<Long, Boolean> entry : userCheckBox.entrySet()) {
                        if (entry.getValue()) {

                            UserEntity user = userDao.getByChatId(entry.getKey());
                            kb.next(2);
                            kb.addButton("Да", Json.set("step", "S_Interv").set("chatid", chatId));
                            kb.addButton("Нет", Json.set("step", "").set("chatid", chatId));
                            clearMessage(
                                    bot.sendMessage(new SendMessage()
                                            .setChatId(user.getChatId())
                                            .setText("<b>\uD83D\uDCE3 Опрос от " + userDao.getByChatId(chatId).getUserName() + " " +
                                                    userDao.getByChatId(chatId).getUserSurname() + " </b>\nТекст: " + param.getString("messageText"))
                                            .setReplyMarkup(kb.generate())
                                            .enableHtml(true)
                                    )
                            );
                        }
                    }

                }


            }
            break;
        }

    }


    @Step
    public void S_checkBox() throws Exception {

        if (queryData.containsKey("category")) {

            int categoryId = queryData.getInt("category");
            boolean check = categoryCheckBox.get(categoryId);
            categoryCheckBox.put(categoryId, !check);

            IKeyboard kb = new IKeyboard();
            for (Map.Entry<Integer, Boolean> entry : categoryCheckBox.entrySet()) {
                CategoryEntity category = categoryDao.get(entry.getKey());

                String checkTxt = "❎";
                if (entry.getValue()) checkTxt = "✅";

                kb.next(2);
                kb.addButton(
                        checkTxt,
                        Json.set("step", "S_checkBox")
                                .set("category", category.getId())
                );
                kb.addButton(
                        category.getName(),
                        Json.set("step", "S_clickCt")
                                .set("category", category.getId())
                );
            }

            clearMessage(
                    bot.sendMessage(new SendMessage()
                            .setChatId(chatId)
                            .setText("Выберите получателей и введите текст рассылки")
                            .setReplyMarkup(kb.generate())
                    )
            );

        } else {

            StepParam param = new StepParam(chatId, "S_sendMsg");
            param.set("isUser", false);
            param.set("messageText", inputText);
            redirect("S_sendMsg");
        }

    }


    @Step
    public void S_clickCt() throws Exception {
        categoryCheckBox = new HashMap<>();
        int categoryId = queryData.getInt("category");

        List<CategoryEntity> categoryList = categoryDao.getListForSending(categoryId);
        if (categoryList.size() == 0) {

            userCheckBox = new HashMap<>();
            List<UserEntity> userList = userDao.getListByCategory(categoryId);
            sendUserList(userList);

        } else if (categoryId == 6) {

            userCheckBox = new HashMap<>();
            List<UserEntity> userList = userDao.getListPartner();
            sendUserList(userList);

        } else {
            sendCategoryList(categoryList);
        }

    }


    private void sendCategoryList(List<CategoryEntity> categoryList) throws Exception {
        IKeyboard kb = new IKeyboard();

        for (CategoryEntity category : categoryList) {

            kb.next(2);
            kb.addButton(
                    "✅",
                    Json.set("step", "S_checkBox")
                            .set("category", category.getId())
            );
            kb.addButton(
                    category.getName(),
                    Json.set("step", "S_clickCt")
                            .set("category", category.getId())
            );
            categoryCheckBox.put(category.getId(), true);
        }

        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Выберите получателей и введите текст рассылки")
                        .setReplyMarkup(kb.generate())
                )
        );
    }

    private void sendUserList(List<UserEntity> userList) throws Exception {

        IKeyboard kb = new IKeyboard();
        kb.next();

        for (UserEntity user : userList) {

            String text = user.getUserName() + "/" + user.getPosition();
            if (user.getCompanyName() != null) {
                text = user.getCompanyName();
            }

            kb.addButton(
                    "❎ " + text,
                    Json.set("step", "S_checkBoxUser")
                            .set("userChatId", user.getChatId())
            );
            userCheckBox.put(user.getChatId(), false);
        }

        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Выберите получателей и введите текст рассылки")
                        .setReplyMarkup(kb.generate())
                )
        );
    }


    @Step
    public void S_answerEmployee() throws Exception {
        chatidForAnswer = queryData.getLong("chatid");
        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Введите текст для ответа")
                )
        );
        step = "S_answerEmployee2";

    }

    @Step
    public void S_answerEmployee2() throws Exception {

        UserEntity mydata = userDao.getByChatId(chatId);
        UserEntity user = userDao.getByChatId(chatidForAnswer);
        IKeyboard kb = new IKeyboard();
        kb.next(2);
        kb.addButton("Ответить", Json.set("step", "S_answerEmployee").set("chatid", chatId));
        kb.addButton("Пропустить", Json.set("step", "M_main_menu").set("chatid", chatId));
        bot.sendMessage(new SendMessage()
                .setChatId(chatidForAnswer)
                .setText("<b>✉️ Ответ от " + mydata.getUserName() + " " +
                        mydata.getUserSurname() + " </b>\nДолжность: " +
                        mydata.getPosition() + "\nТекст: " + inputText)
                .enableHtml(true)
        );

        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatidForAnswer)
                        .setText("Выберите действие")
                        .setReplyMarkup(kb.generate())
                        .enableHtml(true)
                )
        );

    }


    @Step("⚒ Конструктив")
    public void S_constructive() throws Exception {

        List<CategoryEntity> list = categoryDao.getListByParent(6);
        IKeyboard kb = new IKeyboard();
        for (CategoryEntity constructive : list) {
            kb.next();
            kb.addButton(constructive.getName(), Json.set("step", "S_constr_part").set("constructive", constructive.getParent()));

        }
        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Выберите конструктив")
                        .setReplyMarkup(kb.generate())
                        .enableHtml(true)
                )
        );


    }


    @Step
    public void S_constr_part() throws Exception {

        int category = queryData.getInt("constructive");
        List<UserEntity> list = userDao.getListByCategoryRecusiv(category);

        IKeyboard kb = new IKeyboard();
        for (UserEntity constructive : list) {
            kb.next();
            kb.addButton(constructive.getCompanyName(), Json.set("step", "S_constr_part").set("chatid", constructive.getChatId()));

        }
        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Выберите конструктив")
                        .setReplyMarkup(kb.generate())
                        .enableHtml(true)
                )
        );

    }


    @Step("Партнеры")
    public void S_partners() throws Exception {

        List<UserEntity> list = userDao.getListByCategoryRecusiv(6);
        IKeyboard kb = new IKeyboard();
        for (UserEntity constructive : list) {
            kb.next(1);
            kb.addButton(constructive.getCompanyName(), Json.set("step", "S_send_part").set("constructive", constructive.getChatId()));

        }
        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Партнеры")
                        .setReplyMarkup(kb.generate())
                        .enableHtml(true)
                )
        );
    }


    @Step
    public void S_send_part() throws Exception {
        chatidForPartners = queryData.getLong("constructive");
        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("Введите текст сообщения")
                        .enableHtml(true)
                )
        );

        step = "S_send_part2";
    }


    @Step
    public void S_send_part2() throws Exception {
        UserEntity mydata = userDao.getByChatId(chatId);
        UserEntity user = userDao.getByChatId(chatidForPartners);
        IKeyboard kb = new IKeyboard();
        kb.next(2);
        kb.addButton("Ответить", Json.set("step", "S_answerEmployee").set("chatid", chatId));
        kb.addButton("Пропустить", Json.set("step", "M_main_menu").set("chatid", chatId));
        bot.sendMessage(new SendMessage()
                .setChatId(chatidForPartners)
                .setText("<b>✉️ Сообщения от " + mydata.getUserName() + " " +
                        mydata.getUserSurname() + " </b>\nДолжность: " +
                        mydata.getPosition() + "\nТекст: " + inputText)
                .enableHtml(true)
        );

        clearMessage(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatidForPartners)
                        .setText("Выберите действие")
                        .setReplyMarkup(kb.generate())
                        .enableHtml(true)
                )
        );

    }




    @Step
    public void S_checkBoxUser() throws Exception {


        if (queryData.containsKey("userChatId")) {

            long userChatId = queryData.getLong("userChatId");
            boolean check = userCheckBox.get(userChatId);
            userCheckBox.put(userChatId, !check);

            IKeyboard kb = new IKeyboard();
            for (Map.Entry<Long, Boolean> entry : userCheckBox.entrySet()) {
                UserEntity user = userDao.getByChatId(entry.getKey());

                String checkTxt = "❎";
                if (entry.getValue()) checkTxt = "✅";

                String text = user.getUserName() + "/" + user.getPosition();
                if (user.getCompanyName() != null){
                    text = user.getCompanyName();
                }

                kb.next(2);
                kb.addButton(
                        checkTxt + " " + text,
                        Json.set("step", "S_checkBoxUser")
                                .set("userChatId", user.getId()) /// кажется все
                );
            }

            clearMessage(
                    bot.sendMessage(new SendMessage()
                            .setChatId(chatId)
                            .setText("Выберите получателей и введите текст рассылки")
                            .setReplyMarkup(kb.generate())
                    )
            );

        } else {

            StepParam param = new StepParam(chatId, "S_sendMsg");
            param.set("isUser", true);
            param.set("messageText", inputText);
            redirect("S_sendMsg");
        }

       }




}
