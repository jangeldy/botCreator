package database;

import database.dao.UserDao;
import database.dao.UserDaoImpl;

import javax.sql.DataSource;

public class DaoFactory {

    private DaoFactory(){}

    private static DataSource source;
    private static DaoFactory ourInstance = new DaoFactory();

    public void setDataSource(DataSource dataSource) {
        source = dataSource;
    }

    public static DaoFactory getInstance() {
        return ourInstance;
    }

    public UserDao getUserDao() {
        return new UserDaoImpl(source);
    }
}
