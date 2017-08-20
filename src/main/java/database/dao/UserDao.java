package database.dao;

import database.entity.UserEntity;
import org.springframework.stereotype.Component;
import pro.nextbit.telegramconstructor.database.DataTable;

import java.util.List;

@Component

public interface UserDao {

    DataTable getTable();

}
