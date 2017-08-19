package database.dao;

import database.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface UserDao {

    UserEntity getByChatId(long chatId);

    int insert(UserEntity userEntity);

    boolean checkUser(long chatId);

    List<UserEntity> getListByCategory(int category);

    List<UserEntity> getListByCategoryRecusiv(int category);

    List<UserEntity> getListPartner();


}
