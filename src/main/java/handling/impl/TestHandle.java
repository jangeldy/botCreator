package handling.impl;

import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TestHandle extends AbstractHandle {

    private int sss = 0;

    @Override
    public void handling() throws TelegramApiException {
        sss++;

        switch (step){

            // описание шага
            case "STEP_1" : {


                bot.sendMessage(
                        new SendMessage()
                                .setChatId(chatId)
                                .setText(String.valueOf(sss))
                );

                if (sss > 3) redirect("STEP_2");


            } break;

            // описание шага
            case "STEP_2" : {

                bot.sendMessage(
                        new SendMessage()
                        .setChatId(chatId)
                        .setText("test")
                );


            } break;

        }
    }
}
