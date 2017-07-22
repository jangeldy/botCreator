package database.dao;

import database.entity.UsersEntity;

public interface UsersDao {

    UsersEntity get(int id);

    boolean checkUser(long chatId);
}
