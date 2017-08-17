import bgtasks.SchedulingConfig;
import bgtasks.SchedulingTasks;
import util.database.AppProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import util.stepmapping.StepMapping;

public class Main {

    @SuppressWarnings({"unused", "resource"})
    public static void main(String[] args) {

        Logger log = LogManager.getLogger("Main");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {

            log.info("Running telegrams bot...");
            initialize();

            Bot bot = new Bot();
            SchedulingTasks.setBot(bot);
            bot.setBotUserName(AppProperties.getProp("botUserName"));
            bot.setBotToken(AppProperties.getProp("botToken"));

            telegramBotsApi.registerBot(bot);
            log.info("Telegrams bot is running");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initialize() throws Exception {
        AppProperties.init("app.properties");
        StepMapping.initializeMapping();
        new AnnotationConfigApplicationContext(SchedulingConfig.class);
        ApiContextInitializer.init();
    }
}
