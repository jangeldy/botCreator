package database.dao;

import database.entity.UsersEntity;
import util.databaseconfig.ut.DataBaseUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UsersDaoImpl implements UsersDao {

    private DataBaseUtils utils;

    public UsersDaoImpl(DataSource source) {
        this.utils = new DataBaseUtils(source);
    }

    @Override
    public UsersEntity get(int id) {
        return utils.queryForObject("SELECT * FROM users WHERE id = ?",
                new Object[] { id }, this::mapper);
    }


    @Override
    public boolean checkUser(long chatId) {
        List<UsersEntity> list = utils.query("SELECT * FROM users WHERE chat_id = ?",
                new Object[] { chatId }, this::mapper);
        return list.size() > 0;
    }

    private UsersEntity mapper(ResultSet rs, int index) throws SQLException {

        UsersEntity u = new UsersEntity();
        u.setId(rs.getInt("id"));
        u.setIdAccessLevel(rs.getInt("id_access_level"));
        u.setIdPosition(rs.getInt("id_position"));
        u.setIdConstructive(rs.getInt("id_constructive"));
        u.setChatId(rs.getLong("chat_id"));
        u.setUserName(rs.getString("user_name"));
        u.setUserSurname(rs.getString("user_surname"));
        u.setCompanyName(rs.getString("companyname"));

        return u;
    }
}
