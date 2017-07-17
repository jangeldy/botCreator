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

            log.info("Reading data from a file telegram.properties...");
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("telegram.properties");
            properties.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error! The file telegram.properties was not found or an error occurred while reading the data");
        }

    }

    public String getProp(String name) {
        return properties.getProperty(name);
    }

}
