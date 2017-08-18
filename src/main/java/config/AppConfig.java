package config;

import bgtasks.SchedulingConfig;
import bgtasks.SchedulingTasks;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.telegram.telegrambots.ApiContextInitializer;
import util.accesslevel.AccessLevelMap;
import util.database.AppProperties;
import util.handle.Bot;
import util.stepmapping.StepMapping;

import javax.sql.DataSource;

public class AppConfig {

    public Bot initBot() throws Exception {

        new AppProperties().init("app.properties");
        new AccessLevelMap().init(dataSource());
        StepMapping.initializeMapping();
        new AnnotationConfigApplicationContext(SchedulingConfig.class);
        ApiContextInitializer.init();

        Bot bot = new Bot();
        bot.setBotUserName(AppProperties.getProp("botUserName"));
        bot.setBotToken(AppProperties.getProp("botToken"));
        SchedulingTasks.setBot(bot);

        return bot;
    }

    public static DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(AppProperties.getProp("jdbc.driverClassName"));
        driver.setUrl(AppProperties.getProp("jdbc.url"));
        driver.setUsername(AppProperties.getProp("jdbc.username"));
        driver.setPassword(AppProperties.getProp("jdbc.password"));
        return driver;
    }
}
