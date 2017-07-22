package database.dao;

import database.entity.PositionEntity;
import util.databaseconfig.ut.DataBaseUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PositionDaoImpl implements PositionDao {

    private DataBaseUtils utils;

    public PositionDaoImpl(DataSource source) {
        this.utils = new DataBaseUtils(source);
    }

    @Override
    public List<PositionEntity> getList(){
        return utils.query("SELECT * FROM position", this::mapper);
    }

    private PositionEntity mapper(ResultSet rs, int index) throws SQLException {
        PositionEntity p = new PositionEntity();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        return p;
    }
}
