package database.dao;

import database.entity.UserEntity;
import util.database.DataBaseUtils;

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
    public UserEntity getByChatId(long chatId) {
        return utils.queryForObject("select * from users where chat_id = ?",
                new Object[] { chatId }, this::mapper
        );
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

    @Override
    public List<UserEntity> getListByCategory(int category) {
        return utils.query("SELECT * FROM users WHERE id_category = ?",
                new Object[] { category }, this::mapper);
    }

    @Override
    public List<UserEntity> getListByCategoryRecusiv(int category) {
        return utils.query(
                "WITH RECURSIVE categoryInner AS (  " +
                        "  SELECT id, parent, name FROM category " +
                        "  WHERE parent = ?  " +
                        "  UNION ALL  " +
                        "  SELECT c.id, c.parent, c.name  " +
                        "  FROM category c, categoryInner r  " +
                        "  WHERE r.id = c.parent ) " +
                        "SELECT u.* FROM categoryInner c " +
                        "INNER JOIN users u ON u.id_category = c.id ",
                new Object[] { category }, this::mapper);
    }

    @Override
    public List<UserEntity> getListPartner() {
        return utils.query("SELECT * FROM users u " +
                        "INNER JOIN category c ON u.id_category = c.id " +
                        "WHERE c.parent = 6",
                this::mapper);
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
