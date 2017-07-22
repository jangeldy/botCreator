package database.dao;

import database.entity.ConstructiveEntity;
import util.databaseconfig.ut.DataBaseUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConstructiveDaoImpl implements ConstructiveDao {

    private DataBaseUtils utils;

    public ConstructiveDaoImpl(DataSource source) {
        this.utils = new DataBaseUtils(source);
    }

    @Override
    public List<ConstructiveEntity> getList() {
        return utils.query("SELECT * FROM constructive", this::mapper);
    }


    private ConstructiveEntity mapper(ResultSet rs, int index) throws SQLException {
        ConstructiveEntity c = new ConstructiveEntity();
        c.setId(rs.getInt("id"));
        c.setName("name");
        return c;
    }
}
