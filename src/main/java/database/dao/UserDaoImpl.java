package database.dao;

import database.entity.UserEntity;
import util.databaseconfig.ut.DataBaseUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private DataBaseUtils utils;

    public UserDaoImpl(DataSource source) {
        this.utils = new DataBaseUtils(source);
    }

    @Override
    public int insert(UserEntity user) {
        return (int) utils.updateForKeyId(
                "INSERT INTO users (chat_id, user_name, user_surname, " +
                        "id_category, position, company_name) " +
                        "VALUES (?,?,?,?,?,?)",
                user.getChatId(), user.getUserName(), user.getUserSurname(),
                user.getIdCatogory(), user.getPosition(), user.getCompanyName()
        );
    }


    @Override
    public boolean checkUser(long chatId) {
        List<UserEntity> list = utils.query("SELECT * FROM users WHERE chat_id = ?",
                new Object[] { chatId }, this::mapper);
        return list.size() > 0;
    }

    private UserEntity mapper(ResultSet rs, int index) throws SQLException {

        UserEntity u = new UserEntity();
        u.setId(rs.getInt("id"));
        u.setIdAccessLevel(rs.getInt("id_access_level"));
        u.setIdCatogory(rs.getInt("id_category"));
        u.setChatId(rs.getLong("chat_id"));
        u.setUserName(rs.getString("user_name"));
        u.setUserSurname(rs.getString("user_surname"));
        u.setPosition(rs.getString("position"));
        u.setCompanyName(rs.getString("company_name"));
        return u;
    }
}
