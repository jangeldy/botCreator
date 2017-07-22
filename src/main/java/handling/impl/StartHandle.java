package handling.impl;

import handling.AbstractHandle;

public class StartHandle extends AbstractHandle {

/*    @Step(value = "start", commandText = "/start")
    public void start() throws Exception {

        if (dbUtils.query("SELECT * FROM users WHERE chat_id = ?", chatId))
        DataTable table = dbUtils.query("SELECT * FROM position");
        IKeyboard kb = new IKeyboard();
        kb.next();

        for (DataRec rec : table){
            kb.addButton(
                    rec.getString("name"),
                    Json.set("pos_id", rec.getInt("id"))
            );
        }

        setMessageToClear(
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText("К какой позиций вы относитесь?")
                        .setReplyMarkup(kb.generate())
                )
        );
    }*/
}
