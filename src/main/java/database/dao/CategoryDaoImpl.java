package database.dao;

import database.entity.CategoryEntity;
import util.databaseconfig.ut.DataBaseUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private DataBaseUtils utils;

    public CategoryDaoImpl(DataSource source) {
        this.utils = new DataBaseUtils(source);
    }

    @Override
    public List<CategoryEntity> getListByParent(int parent){

        String parentStr = " = " + String.valueOf(parent);
        if (parent < 1) parentStr = " is null ";

        return utils.query(
                "SELECT * FROM category WHERE parent "
                        + parentStr + " AND id NOT IN (4,5)",
                this::mapper);
    }

    private CategoryEntity mapper(ResultSet rs, int index) throws SQLException {
        CategoryEntity p = new CategoryEntity();
        p.setId(rs.getInt("id"));
        p.setParent(rs.getInt("parent"));
        p.setName(rs.getString("name"));
        return p;
    }
}
