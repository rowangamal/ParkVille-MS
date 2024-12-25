package com.example.backend.repository;

import com.example.backend.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DriverRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Driver driver){
        String sqlStatement = "insert into Driver " +
                "(username, email, license_plate_number, password, payment_method) " +
                "values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlStatement, driver.getUsername(), driver.getEmail(), driver.getLicensePlateNumber(), driver.getPassword(), driver.getPaymentMethod());
    }

    public List<Driver> getAll(){
        String sqlStatement = "select * from Driver";

        RowMapper<Driver> mapper = new RowMapper<Driver>() {
            @Override
            public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
                Driver driver = new Driver();
                driver.setId(rs.getInt(1));
                driver.setUsername(rs.getString(2));
                driver.setEmail(rs.getString(3));
                driver.setLicensePlateNumber(rs.getString(4));
                driver.setPassword(rs.getString(5));
                driver.setPaymentMethod(rs.getString(6));
                return driver;
            }
        };
        return jdbcTemplate.query(sqlStatement, mapper);
    }
}
