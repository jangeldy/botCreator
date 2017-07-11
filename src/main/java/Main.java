import bgtasks.SchedulingConfig;
import bgtasks.SchedulingTasks;
import database.DbConnProps;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import util.TelegramProps;

public class Main {

    @SuppressWarnings({"unused", "resource"})
    public static void main(String[] args) {

        Logger log = LogManager.getLogger("Main");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {

            log.info("Запуск телеграм бота...");
            initialize();

            Bot bot = new Bot();
            TelegramProps props = new TelegramProps();
            SchedulingTasks.setBot(bot);
            bot.setBotUserName(props.getProp("botUserName"));
            bot.setBotToken(props.getProp("botToken"));

            telegramBotsApi.registerBot(bot);
            log.info("Телеграм бот запущен");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initialize() {
        new AnnotationConfigApplicationContext(SchedulingConfig.class);
        new DbConnProps().initProperty();
        ApiContextInitializer.init();
    }
}
