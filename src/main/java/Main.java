import config.AppConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.TelegramBotsApi;
import pro.nextbit.telegramconstructor.database.AppProperties;

public class Main {

    @SuppressWarnings({"unused", "resource"})
    public static void main(String[] args) {

        Logger log = LogManager.getLogger("Main");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {

            log.info("Running telegrams bot...");
            new AppProperties().init("app.properties");
            new AnnotationConfigApplicationContext(AppConfig.class);
            AppConfig config = new AppConfig();
            telegramBotsApi.registerBot(config.initBot());
            log.info("Telegrams bot is running");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
