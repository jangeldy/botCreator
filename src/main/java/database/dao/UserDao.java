package database.dao;

import database.entity.UserEntity;

import java.util.List;

public interface UserDao {

    UserEntity getByChatId(long chatId);

    int insert(UserEntity userEntity);

    boolean checkUser(long chatId);

    List<UserEntity> getListByCategory(int category);

    List<UserEntity> getListByCategoryRecusiv(int category);

    List<UserEntity> getListPartner();


}
