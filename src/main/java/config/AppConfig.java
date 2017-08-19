package config;

import bgtasks.SchedulingTasks;
import database.dao.UserDao;
import database.dao.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import pro.nextbit.telegramconstructor.accesslevel.AccessLevelMap;
import pro.nextbit.telegramconstructor.database.AppProperties;
import pro.nextbit.telegramconstructor.handle.Bot;
import pro.nextbit.telegramconstructor.stepmapping.StepMapping;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
public class AppConfig {

    public Bot initBot() throws Exception {

        new AccessLevelMap().init(getDataSource());
        StepMapping.initializeMapping("handling");
        ApiContextInitializer.init();

        Bot bot = new Bot();
        bot.setBotUserName(AppProperties.getProp("botUserName"));
        bot.setBotToken(AppProperties.getProp("botToken"));

        return bot;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(AppProperties.getProp("jdbc.driverClassName"));
        driver.setUrl(AppProperties.getProp("jdbc.url"));
        driver.setUsername(AppProperties.getProp("jdbc.username"));
        driver.setPassword(AppProperties.getProp("jdbc.password"));
        return driver;
    }

    @Bean
    public SchedulingTasks getSchedulingTasks() {
        return new SchedulingTasks();
    }

    @Bean
    public UserDao getUserDao() {
        return new UserDaoImpl(getDataSource());
    }
}
