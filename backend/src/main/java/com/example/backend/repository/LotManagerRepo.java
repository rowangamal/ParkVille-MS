package com.example.backend.repository;

import com.example.backend.DTOs.LotCreationDTO;
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
        String sqlStatement = "insert into Parking_Lot_Manager " +
                "(username, email, password) " +
                "values ( ?, ?, ?)";

        jdbcTemplate.update(sqlStatement,  lotManager.getUsername(), lotManager.getEmail(), lotManager.getPassword());
    }

    public List<LotManager> getAll(){
        String sqlStatement = "select * from Parking_Lot_Manager";

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

    public LotManager findManagerById(int id){
        String sqlStatement = "select * from Parking_Lot_Manager where id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, new Object[]{id}, (resultSet, i) -> {
            LotManager lotManager = new LotManager();
            lotManager.setId(resultSet.getInt("id"));
            lotManager.setUsername(resultSet.getString("username"));
            lotManager.setEmail(resultSet.getString("email"));
            lotManager.setPassword(resultSet.getString("password"));
            return lotManager;
        });
    }
}
