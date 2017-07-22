package database;

import database.dao.*;
import util.databaseconfig.DataBaseConfig;

import javax.sql.DataSource;

public class DaoFactory {

    private static DataSource getDataSource(){
        return DataBaseConfig.dataSource();
    }

    public PositionDao getPositionDao() {
        return new PositionDaoImpl(getDataSource());
    }

    public ConstructiveDao getConstructivDao() {
        return new  ConstructiveDaoImpl(getDataSource());
    }

    public UsersDao getUsersDao() {
        return new UsersDaoImpl(getDataSource());
    }
}
