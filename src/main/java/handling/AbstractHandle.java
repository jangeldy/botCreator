package handling;

import database.DaoFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import util.AccessLevel;
import util.GlobalParam;
import util.databaseconfig.ut.DataRec;
import util.stepmapping.Mapping;
import util.stepmapping.StepMapping;

import java.util.List;

public abstract class AbstractHandle {

    protected Logger log;
    protected DaoFactory daoFactory;
    protected TelegramLongPollingBot bot;
    protected Update update;

    // параметры команды
    protected String step;
    protected String inputText;
    protected DataRec queryData;
    protected AccessLevel accessLevel;
    protected int messageId;
    protected long chatId;

    //
    private List<Integer> messageToClear;
    private Mapping redirectMapping;


    /**
     * Параметры
     * @param bot - bot
     * @param update - update
     * @param globalParam - globalParam
     */
    public void setGlobalParam(
            TelegramLongPollingBot bot,
            Update update,
            GlobalParam globalParam,
            List<Integer> messageToClear
    ){
        this.log = LogManager.getLogger(this.getClass().getSimpleName());
        this.bot = bot;
        this.update = update;
        this.daoFactory = new DaoFactory();

        this.inputText = globalParam.getInputText();
        this.redirectMapping = new Mapping();
        this.queryData = globalParam.getQueryData();
        this.accessLevel = globalParam.getAccessLevel();
        this.messageId = globalParam.getMessageId();
        this.chatId = globalParam.getChatId();
        this.messageToClear = messageToClear;
    }

    public String getChangedStep() {
        return step;
    }

    public Mapping getRedirect(){
        return redirectMapping;
    }


    /**
     * Перенапрвления команды
     * @param step - шаг
     */
    protected void redirect(String step){

        if (step == null
                || step.trim().equals("")
                || StepMapping.containsStep(step)){
            throw new RuntimeException("Error in redirect command. Step can not be empty");
        }

        redirectMapping = StepMapping.getMappingByStep(step);
        redirectMapping.setRedirect(true);

    }


    /**
     * Для очистки сообщения
     * @param msgId - идентфикатор
     */
    protected void setMessageToClear(int msgId){
        messageToClear.add(msgId);
    }

    /**
     * Для очистки сообщения
     * @param message - сообщение
     */
    protected void setMessageToClear(Message message){
        messageToClear.add(message.getMessageId());
    }

}
