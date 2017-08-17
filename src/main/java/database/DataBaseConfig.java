package database;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import util.database.AppProperties;

import javax.sql.DataSource;

class DataBaseConfig {

    static DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(AppProperties.getProp("jdbc.driverClassName"));
        driver.setUrl(AppProperties.getProp("jdbc.url"));
        driver.setUsername(AppProperties.getProp("jdbc.username"));
        driver.setPassword(AppProperties.getProp("jdbc.password"));
        return driver;
    }
}
