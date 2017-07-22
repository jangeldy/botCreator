package util.databaseconfig;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DataBaseConfig {

    public static DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(DataBaseProps.getProp("jdbc.driverClassName"));
        driver.setUrl(DataBaseProps.getProp("jdbc.url"));
        driver.setUsername(DataBaseProps.getProp("jdbc.username"));
        driver.setPassword(DataBaseProps.getProp("jdbc.password"));
        return driver;
    }
}
