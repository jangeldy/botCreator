package database.dao;

import database.entity.UsersEntity;

import java.util.List;

public interface UsersDao {

    UsersEntity get(int id);

    boolean checkUser(long chatId);
}
