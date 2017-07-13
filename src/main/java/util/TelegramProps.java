package util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class TelegramProps {

    private Properties properties;

    public TelegramProps() {
        properties = new Properties();
        getProps();
    }

    private void getProps() {

        Logger log = LogManager.getLogger(this.getClass().getSimpleName());

        try {

            log.info("Чтение данных с файла telegram.properties...");
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("telegram.properties");
            properties.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("!Ошибка. Файл telegram.properties не найден либо произошла ошибка при считываний");
        }

    }

    public String getProp(String name) {
        return properties.getProperty(name);
    }

}
