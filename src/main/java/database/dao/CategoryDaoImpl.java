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
    public List<CategoryEntity> getList(){
        return utils.query("SELECT * FROM position", this::mapper);
    }

    private CategoryEntity mapper(ResultSet rs, int index) throws SQLException {
        CategoryEntity p = new CategoryEntity();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        return p;
    }
}
