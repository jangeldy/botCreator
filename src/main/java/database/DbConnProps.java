package database;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.InputStream;
import java.util.Properties;

public class DbConnProps {

    private static Properties properties = new Properties();

    public void initProperty() {

        Logger log = LogManager.getLogger("DbConnProps");

        try {

            log.info("Чтение данных с файла app.properties...");
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("app.properties");
            properties.load(inputStream);

        }catch (Exception e){
            e.printStackTrace();
            log.info("!Ошибка. Файл app.properties не найден или произошла ошибка при считываний");
        }

    }

    static String getProp(String propertyName){
        return properties.getProperty(propertyName);
    }

}
