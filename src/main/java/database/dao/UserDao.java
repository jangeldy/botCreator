package database.dao;

import database.entity.UserEntity;

public interface UserDao {

    UserEntity get(int id);

    boolean checkUser(long chatId);
}
