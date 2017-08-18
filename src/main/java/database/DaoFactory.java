package database;

import config.AppConfig;
import database.dao.*;

import javax.sql.DataSource;

public class DaoFactory {

    private DaoFactory(){}

    private static DataSource source = AppConfig.dataSource();
    private static DaoFactory ourInstance = new DaoFactory();

    public static DaoFactory getInstance() {
        return ourInstance;
    }

    public UserDao getUserDao() {
        return new UserDaoImpl(source);
    }
}
