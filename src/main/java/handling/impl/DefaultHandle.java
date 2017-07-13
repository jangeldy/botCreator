package handling.impl;

import handling.AbstractHandle;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class DefaultHandle extends AbstractHandle {

    @Override
    public void handling() throws TelegramApiException {
        redirect("STEP_1");
    }
}
