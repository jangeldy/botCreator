import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import handling.AbstractHandle;
import handling.Mapping;
import database.utils.DataRec;
import database.utils.DataTable;
import database.utils.DbUtils;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import util.*;

import java.lang.reflect.Type;

class Handling {

    private String step;
    private String lastHandlingClass;


    /**
     * Обработка входящих команд
     * @param bot    - бот
     * @param update - объект входящего запроса
     */
    void start(Bot bot, Update update, Message message) {

        Command command = getCommand(update, message.getChatId());
        if (message.isUserMessage() &&
                command.getAccessLevel() != AccessLevel.WITHOUT_ACCESS) {

            try {

                checkCommand(command);
                AbstractHandle handle = Mapping.getHandleClass(command.getHandlingClass());
                handle.setGlobalParam(bot, update, command);
                handle.executeHandling();
                step = command.getStep();

                while (handle.getRedirectCommand() != null
                        && handle.getRedirectCommand().getStep() != null) {

                    command = handle.getRedirectCommand();
                    if (command.getHandlingClass() == null){
                        String hc = getHandlingClass(command.getStep());
                        command.setHandlingClass(hc);
                    }
                    checkCommand(command);

                    handle = Mapping.getHandleClass(command.getHandlingClass());
                    handle.setGlobalParam(bot, update, command);
                    handle.executeHandling();
                    step = command.getStep();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCommand(Command command){
        if (command.getHandlingClass() == null) {
            if (lastHandlingClass == null){
                lastHandlingClass = "DefaultHandle";
            }

            command.setHandlingClass(lastHandlingClass);
            command.setStep(step);

        } else {
            lastHandlingClass = command.getHandlingClass();
            step = command.getStep();
        }
    }


    /**
     * Ищет команду по базе
     * @param update - объект входящего запроса
     * @return - commandEntity
     */
    private Command getCommand(Update update, long chatId) {

        Command command = new Command();
        AccessLevel access = getAccessLevel(chatId);
        DataRec queryData = getQueryData(update);
        String commandText;
        int messageId;

        DbUtils dbUtils = new DbUtils();
        DataTable dataTable = null;


        if (update.getMessage() == null) {

            commandText = update.getCallbackQuery().getMessage().getText();
            messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (queryData.containsKey("step")) {
                dataTable = dbUtils.query("SELECT * FROM command WHERE step = ?",
                        queryData.getString("step"));
            }

        } else {

            commandText = update.getMessage().getText();
            messageId = update.getMessage().getMessageId();
            dataTable = dbUtils.query("SELECT * FROM command WHERE command_text = ?", commandText);

            if (dataTable.size() > 1) {
                throw new RuntimeException("Найдено более двух команд \"" + commandText + "\"");
            }
        }

        if (dataTable != null && dataTable.size() == 1) {

            DataRec rec = dataTable.get(0);
            command.setHandlingClass(rec.getString("handling_class"));
            command.setStep(rec.getString("step"));

        } else if (queryData.containsKey("handling_class")) {
            command.setHandlingClass(queryData.getString("handling_class"));
        }

        command.setMessageId(messageId);
        command.setCommandText(commandText);
        command.setChatId(chatId);
        command.setAccessLevel(access);
        command.setQueryData(queryData);
        return command;
    }


    private String getHandlingClass(String step) {
        DbUtils dbUtils = new DbUtils();
        DataRec rec = dbUtils.queryDataRec("SELECT * FROM command WHERE step = ?", step);
        return rec.getString("handling_class");
    }


    /**
     * Ищет скрытые данные
     * @param update - объект входящего запроса
     * @return - getQueryData
     */
    private DataRec getQueryData(Update update) {

        DataRec queryData = new DataRec();

        if (update.getMessage() == null) {
            String queryText = update.getCallbackQuery().getData();

            if (queryText != null && !queryText.equals("")) {
                try {
                    Type token = new TypeToken<DataRec>(){}.getType();
                    queryData = new Gson().fromJson(queryText, token);

                    for (DataRec.Entry<String, Object> entry:queryData.entrySet()){
                        if (entry.getValue() instanceof Double){
                            Double value = (Double) entry.getValue();
                            entry.setValue(value.intValue());
                        }
                    }
                } catch (Exception ignore) {}
            }
        }

        return queryData;
    }


    /**
     * Получение уровня доступа
     * @param chatId - id чата
     * @return - AccessLevel
     */
    private AccessLevel getAccessLevel(long chatId) {

        AccessLevel accessLevel = AccessLevel.READ;
        DbUtils dbUtils = new DbUtils();
        DataTable dataTable = dbUtils.query(
                "SELECT a.enum_name FROM users u " +
                        "INNER JOIN access_level a ON u.id_access_level = a.id " +
                        "WHERE u.chat_id = ?", chatId
        );

        if (dataTable.size() == 1) {
            String enumName = dataTable.get(0).getString("enum_name");
            accessLevel = AccessLevel.valueOf(enumName);
        }

        return accessLevel;
    }

}
