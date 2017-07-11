package database;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DbConn {

    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(DbConnProps.getProp("jdbc.driverClassName"));
        driver.setUrl(DbConnProps.getProp("jdbc.url"));
        driver.setUsername(DbConnProps.getProp("jdbc.username"));
        driver.setPassword(DbConnProps.getProp("jdbc.password"));
        return driver;
    }
}
