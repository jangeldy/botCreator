package database.dao;

import database.entity.CategoryEntity;

import java.util.List;

public interface CategoryDao {

    CategoryEntity get(int id);

    List<CategoryEntity> getListByParent(int parent);

    List<CategoryEntity> getListForSending(int parent);

}
