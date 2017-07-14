package handling;

import database.utils.DataRec;
import database.utils.DbUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.AccessLevel;
import util.Command;

public abstract class AbstractHandle {

    protected Logger log;
    protected DbUtils dbUtils;
    protected TelegramLongPollingBot bot;
    protected Update update;

    // параметры команды
    protected String step;
    protected String commandText;
    protected DataRec queryData;
    protected AccessLevel accessLevel;
    protected int messageId;
    protected long chatId;

    // перенаправление команды
    private Command redirectCommand;
    private Command command;

    /**
     * Параметры
     * @param bot - bot
     * @param update - update
     * @param command - command
     */
    public void setGlobalParam(
            TelegramLongPollingBot bot,
            Update update,
            Command command
    ){
        this.log = LogManager.getLogger(this.getClass().getSimpleName());
        this.dbUtils = new DbUtils();
        this.bot = bot;
        this.update = update;

        this.command = command;
        this.step = command.getStep();
        this.commandText = command.getCommandText();
        this.queryData = command.getQueryData();
        this.accessLevel = command.getAccessLevel();
        this.messageId = command.getMessageId();
        this.chatId = command.getChatId();
        this.redirectCommand = new Command();
    }

    public void executeHandling() throws TelegramApiException {

        String redirect = "";
        if (command.isRedirect()){
            redirect = "REDIRECT --> ";
        }

        log.info("-----> " + redirect +
                "CLASSNAME : " + this.getClass().getSimpleName() + ";  " +
                "STEP : " + step + ";");

        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(chatId));
            deleteMessage.setMessageId(messageId);
            bot.deleteMessage(deleteMessage);
        } catch (Exception ignore){}

        handling();
        command.setStep(step);
    }

    public abstract void handling() throws TelegramApiException;

    public Command getRedirectCommand(){
        return redirectCommand;
    }

    /**
     * Перенапрвления команды
     * @param rCommand - параметры команды
     */
    protected void redirect(Command rCommand){

        if (rCommand.getChatId() == -1){
            rCommand.setChatId(command.getChatId());
        }

        if (rCommand.getAccessLevel() == null){
            rCommand.setAccessLevel(command.getAccessLevel());
        }

        if (rCommand.getStep() == null){
            throw new RuntimeException("Ошибка при перенаправлений команды. Не задан 'step'!");
        }

        redirectCommand = rCommand;
        redirectCommand.setRedirect(true);
    }

    /**
     * Перенапрвления команды
     * @param step - шаг
     */
    protected void redirect(String step){

        redirectCommand.setRedirect(true);
        redirectCommand.setStep(step);

        if (redirectCommand.getChatId() == -1){
            redirectCommand.setChatId(command.getChatId());
        }

        if (redirectCommand.getAccessLevel() == null){
            redirectCommand.setAccessLevel(command.getAccessLevel());
        }

        if (redirectCommand.getStep() == null){
            throw new RuntimeException("Ошибка при перенаправлений команды. Не задан 'step'!");
        }

    }


    /**
     * Перенапрвления команды
     * @param clazz - обрабатывающий класс
     * @param step - шаг
     */
    protected void redirect(Class clazz, String step){

        redirectCommand.setRedirect(true);
        redirectCommand.setStep(step);
        redirectCommand.setHandlingClass(clazz.getSimpleName());

        if (redirectCommand.getChatId() == -1){
            redirectCommand.setChatId(command.getChatId());
        }

        if (redirectCommand.getAccessLevel() == null){
            redirectCommand.setAccessLevel(command.getAccessLevel());
        }

        if (redirectCommand.getStep() == null){
            throw new RuntimeException("Ошибка при перенаправлений команды. Не задан 'step'!");
        }

    }

}
