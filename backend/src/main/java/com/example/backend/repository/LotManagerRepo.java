package com.example.backend.repository;

import com.example.backend.model.LotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LotManagerRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(LotManager lotManager){
        String sqlStatement = "insert into parking_lot_manager " +
                "(id, username, email, password) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(sqlStatement, lotManager.getId(), lotManager.getUsername(), lotManager.getEmail(), lotManager.getPassword());
    }

    public List<LotManager> getAll(){
        String sqlStatement = "select * from parking_lot_manager";

        RowMapper<LotManager> mapper = new RowMapper<LotManager>() {
            @Override
            public LotManager mapRow(ResultSet rs, int rowNum) throws SQLException {
                LotManager lotManager = new LotManager();
                lotManager.setId(rs.getInt(1));
                lotManager.setUsername(rs.getString(2));
                lotManager.setEmail(rs.getString(3));
                lotManager.setPassword(rs.getString(4));
                return lotManager;
            }
        };
        return jdbcTemplate.query(sqlStatement, mapper);
    }
}