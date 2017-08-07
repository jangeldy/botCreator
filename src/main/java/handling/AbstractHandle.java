package handling;

import database.DaoFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import util.AccessLevel;
import util.ClearMessage;
import util.GlobalParam;
import util.StepParam;
import util.databaseconfig.ut.DataRec;
import util.stepmapping.Mapping;
import util.stepmapping.StepMapping;

import java.util.List;

public class AbstractHandle {

    protected Logger log = LogManager.getLogger(this.getClass());
    protected DaoFactory daoFactory = DaoFactory.getInstance();
    protected TelegramLongPollingBot bot;
    protected Update update;

    // параметры команды
    protected String step;
    protected String inputText;
    protected DataRec queryData;
    protected AccessLevel accessLevel;
    protected DataRec param;
    protected long chatId;

    //
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
            String step
    ){
        this.bot = bot;
        this.update = update;
        this.inputText = globalParam.getInputText();
        this.queryData = globalParam.getQueryData();
        this.accessLevel = globalParam.getAccessLevel();
        this.chatId = globalParam.getChatId();
        this.redirectMapping = new Mapping();
        this.step = step;
        this.param = new StepParam(chatId, step).get();
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
                || !StepMapping.containsStep(step)){
            throw new RuntimeException("Error in redirect command. Step can not be empty");
        }

        redirectMapping = StepMapping.getMappingByStep(step);
        redirectMapping.setRedirect(true);

    }


    /**
     * Для очистки сообщения
     * @param message - сообщение
     */
    protected void clearMessage(Message message){
        ClearMessage.set(message.getChatId(), message.getMessageId());
    }


    protected DataRec setParam(long chatId, String step){
        return new StepParam(chatId, step).get();
    }

    protected DataRec setParam(String step){
        return new StepParam(chatId, step).get();
    }
}
