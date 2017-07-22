package database;

import database.dao.PositionDao;
import database.dao.PositionDaoImpl;
import util.databaseconfig.DataBaseConfig;

import javax.sql.DataSource;

public class DaoFactory {

    private static DataSource getDataSource(){
        return DataBaseConfig.dataSource();
    }

    public PositionDao getPositionDao() {
        return new PositionDaoImpl(getDataSource());
    }
}
