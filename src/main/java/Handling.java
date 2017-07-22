import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import handling.AbstractHandle;
import handling.impl.DefaultHandle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import util.AccessLevel;
import util.GlobalParam;
import util.StepParam;
import util.databaseconfig.DataBaseConfig;
import util.databaseconfig.ut.DataRec;
import util.databaseconfig.ut.DataTable;
import util.databaseconfig.ut.DataBaseUtils;
import util.stepmapping.Mapping;
import util.stepmapping.StepMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class Handling {

    private String step;
    private AbstractHandle handle;
    private List<Integer> messageToClear;
    private Logger log;

    Handling() {
        handle = new DefaultHandle();
        messageToClear = new ArrayList<>();
        this.log = LogManager.getLogger(this.getClass());
    }


    /**
     * Обработка входящих команд
     * @param bot    - бот
     * @param update - объект входящего запроса
     */
    void start(Bot bot, Update update, Message message) {

        GlobalParam globalParam = getGlobalParam(update, message.getChatId());
        Mapping mapping = getMapping(update, globalParam.getQueryData(), step);

        if (message.isUserMessage() &&
                globalParam.getAccessLevel() != AccessLevel.WITHOUT_ACCESS) {

            try {
                mapping = runHandlingMethod(bot, update, globalParam, mapping);
                while (mapping.isRedirect()) {
                    mapping = runHandlingMethod(bot, update, globalParam, mapping);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Ищет команду по базе
     * @param update - объект входящего запроса
     * @param chatId - chatId
     * @return - GlobalParam
     */
    private GlobalParam getGlobalParam(Update update, long chatId) {

        GlobalParam globalParam = new GlobalParam();
        String inputText;
        int messageId;

        if (update.getMessage() == null) {
            inputText = update.getCallbackQuery().getMessage().getText();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
        } else {
            inputText = update.getMessage().getText();
            messageId = update.getMessage().getMessageId();
        }

        globalParam.setMessageId(messageId);
        globalParam.setInputText(inputText);
        globalParam.setChatId(chatId);
        globalParam.setAccessLevel(getAccessLevel(chatId));
        globalParam.setQueryData(getQueryData(update));
        return globalParam;
    }


    /**
     * Ищет класс и метод для выполнения
     * @param update    - объект входящего запроса
     * @param queryData - скрытые данные инлайн кнопки
     * @return - Mapping
     */
    private Mapping getMapping(Update update, DataRec queryData, String step) {

        Mapping mapping = null;
        if (update.getMessage() == null) {

            if (queryData.containsKey("step")) {
                String qd_step = queryData.getString("step");
                if (StepMapping.containsStep(qd_step)) {
                    mapping = StepMapping.getMappingByStep(qd_step);
                }
            }

        } else {

            String commandText = update.getMessage().getText();
            if (StepMapping.containsCommandText(commandText)) {
                mapping = StepMapping.getMappingByCommandText(commandText);
            }
        }

        if (mapping == null) {
            mapping = StepMapping.getMappingByStep(step);
        }

        return mapping;
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

                    for (DataRec.Entry<String, Object> entry : queryData.entrySet()) {
                        if (entry.getValue() instanceof Double) {
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
        DataBaseUtils dbUtils = new DataBaseUtils(DataBaseConfig.dataSource());
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
     * Запуск обрабатывающего метода соответствующего класса
     * @param bot         - bot
     * @param update      - объект входящего запроса
     * @param globalParam - параметры
     * @param mapping     - маппинг
     * @return redirectMapping
     * @throws Exception - Exception
     */
    private Mapping runHandlingMethod(
            Bot bot, Update update,
            GlobalParam globalParam, Mapping mapping
    ) throws Exception {

        // Очистка сообщений
        for (int messageId : messageToClear) {
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(globalParam.getChatId()));
                deleteMessage.setMessageId(messageId);
                bot.deleteMessage(deleteMessage);
            } catch (Exception ignore) {}
        }
        messageToClear.clear();


        // вывод маппинга в консоль
        printMapping(mapping);


        // запсук метода
        String className = mapping.getHandleClassName();

        if (className.equals(handle.getClass().getSimpleName())) {

            Class<?> clazz = handle.getClass();
            handle.setGlobalParam(bot, update, globalParam, messageToClear);

            Method method = clazz.getMethod(mapping.getHandleMethod());
            method.invoke(null);

            StepParam stepParam = new StepParam(globalParam.getChatId(), mapping.getStep());
            stepParam.remove();

            step = handle.getChangedStep();
            return handle.getRedirect();
        }
        else {

            Class<?> clazz = Class.forName("handling.impl." + className);
            Constructor<?> ctor = clazz.getConstructor();

            handle = (AbstractHandle) ctor.newInstance();
            handle.setGlobalParam(bot, update, globalParam, messageToClear);

            Method method = clazz.getMethod(mapping.getHandleMethod());
            method.invoke(null);

            StepParam stepParam = new StepParam(globalParam.getChatId(), mapping.getStep());
            stepParam.remove();

            step = handle.getChangedStep();
            return handle.getRedirect();
        }
    }


    private void printMapping(Mapping mapping) {

        String redirect = "";
        if (mapping.isRedirect()) {
            redirect = "REDIRECT --> ";
        }

        log.info("-----> " + redirect +
                "ClassName : " + mapping.getHandleClassName() + ";  " +
                "MethodName : " + mapping.getHandleMethod() + ";  " +
                "Step : " + mapping.getStep() + ";  " +
                "CommandText : " + mapping.getCommandText() + ";"
        );
    }

}
