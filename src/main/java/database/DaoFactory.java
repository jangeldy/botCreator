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

    public PositionDao getPositionDao() {
        return new PositionDaoImpl(source);
    }

    public ConstructiveDao getConstructivDao() {
        return new  ConstructiveDaoImpl(source);
    }

    public UsersDao getUsersDao() {
        return new UsersDaoImpl(source);
    }
}
