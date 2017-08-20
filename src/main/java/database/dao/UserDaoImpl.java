package database.dao;

import pro.nextbit.telegramconstructor.database.DataBaseUtils;
import pro.nextbit.telegramconstructor.database.DataTable;

import javax.sql.DataSource;

public class UserDaoImpl implements UserDao {

    private DataBaseUtils utils;

    public UserDaoImpl(DataSource source) {
        this.utils = new DataBaseUtils(source);
    }

    @Override
    public DataTable getTable() {
        return utils.query("select * from access_info");
    }
}
