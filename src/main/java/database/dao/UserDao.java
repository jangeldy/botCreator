package database.dao;

import database.entity.UserEntity;

public interface UserDao {

    int insert(UserEntity userEntity);

    boolean checkUser(long chatId);
}
