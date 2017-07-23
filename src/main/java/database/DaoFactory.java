package database;

import database.dao.*;
import util.databaseconfig.DataBaseConfig;

import javax.sql.DataSource;

public class DaoFactory {

    private DaoFactory(){}

    private static DataSource source = DataBaseConfig.dataSource();
    private static DaoFactory ourInstance = new DaoFactory();

    public static DaoFactory getInstance() {
        return ourInstance;
    }

    public CategoryDao getPositionDao() {
        return new CategoryDaoImpl(source);
    }

    public UserDao getUsersDao() {
        return new UserDaoImpl(source);
    }
}
