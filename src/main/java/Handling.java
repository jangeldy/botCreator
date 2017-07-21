import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import database.utils.DataRec;
import database.utils.DataTable;
import database.utils.DbUtils;
import handling.AbstractHandle;
import handling.impl.DefaultHandle;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import util.AccessLevel;
import util.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class Handling {

    private String step;
    private AbstractHandle handle;
    private List<Integer> messageToClear;

    Handling() {
        handle = new DefaultHandle();
        messageToClear = new ArrayList<>();
    }


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
                handle = getHandleClass(command.getHandlingClass());
                handle.setGlobalParam(bot, update, command, messageToClear);
                handle.executeHandling();
                step = command.getStep();
                command = handle.getRedirectCommand();


                while (command.isRedirect()) {

                    if (command.getHandlingClass() == null){
                        String hc = findHandlingClass(command.getStep());
                        command.setHandlingClass(hc);
                    }

                    checkCommand(command);
                    handle = getHandleClass(command.getHandlingClass());
                    handle.setGlobalParam(bot, update, command, messageToClear);
                    handle.executeHandling();
                    step = command.getStep();
                    command = handle.getRedirectCommand();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Проверка команды на параметры
     * @param command - команда
     */
    private void checkCommand(Command command){
        if (command.getHandlingClass() == null) {
            command.setHandlingClass(handle.getClass().getSimpleName());
            command.setStep(step);
        } else {
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
                throw new RuntimeException("Found more than two command_text \"" + commandText + "\"");
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


    /**
     * Поиск обрабатывающего класса
     * в базе по step
     * @param step - шаг
     * @return - String className
     */
    private String findHandlingClass(String step) {
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


    /**
     * Возвращает обрабатывающий класс
     * @param className - наименование класса
     * @return - AbstractHandle
     * @throws Exception - ClassNotFoundException
     */
    private AbstractHandle getHandleClass(String className) throws Exception {

        if (className.equals(handle.getClass().getSimpleName())){
            return handle;
        } else {
            Class<?> clazz = Class.forName("handling.impl." + className);
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();
            return (AbstractHandle) object;
        }
    }

}
