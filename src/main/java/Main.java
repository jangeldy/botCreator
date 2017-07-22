import bgtasks.SchedulingConfig;
import bgtasks.SchedulingTasks;
import util.databaseconfig.DataBaseProps;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import util.stepmapping.StepMapping;
import util.TelegramProps;

public class Main {

    @SuppressWarnings({"unused", "resource"})
    public static void main(String[] args) {

        Logger log = LogManager.getLogger("Main");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {

            log.info("Running telegrams bot...");
            initialize();

            Bot bot = new Bot();
            TelegramProps props = new TelegramProps();
            SchedulingTasks.setBot(bot);
            bot.setBotUserName(props.getProp("botUserName"));
            bot.setBotToken(props.getProp("botToken"));

            telegramBotsApi.registerBot(bot);
            log.info("Telegrams bot is running");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initialize() throws Exception {
        StepMapping.initializeMapping();
        new AnnotationConfigApplicationContext(SchedulingConfig.class);
        new DataBaseProps().initProperty();
        ApiContextInitializer.init();
    }
}
