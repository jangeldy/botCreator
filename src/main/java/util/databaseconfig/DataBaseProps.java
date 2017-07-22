package util.databaseconfig;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseProps {

    private static Properties properties = new Properties();

    public void initProperty() {

        Logger log = LogManager.getLogger(this.getClass());

        try {

            log.info("Reading data from a file app.properties...");
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("app.properties");
            properties.load(inputStream);

        }catch (Exception e){
            e.printStackTrace();
            log.info("Error! The file app.properties was not found or an error occurred while reading the data");
        }

    }

    static String getProp(String propertyName){
        return properties.getProperty(propertyName);
    }

}
