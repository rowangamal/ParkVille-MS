package com.example.backend.repository;

import com.example.backend.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class AdministratorRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Administrator administrator){
        String sqlStatement = "insert into system_adminstrator " +
                "(id, username, email, password) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(sqlStatement, administrator.getId(), administrator.getUsername(), administrator.getEmail(), administrator.getPassword());
    }

    public List<Administrator> getAll(){
        String sqlStatement = "select * from system_adminstrator";

        RowMapper<Administrator> mapper = new RowMapper<Administrator>() {
            @Override
            public Administrator mapRow(ResultSet rs, int rowNum) throws SQLException {
                Administrator administrator = new Administrator();
                administrator.setId(rs.getInt(1));
                administrator.setUsername(rs.getString(2));
                administrator.setEmail(rs.getString(3));
                administrator.setPassword(rs.getString(4));
                return administrator;
            }
        };
        return jdbcTemplate.query(sqlStatement, mapper);
    }
}
