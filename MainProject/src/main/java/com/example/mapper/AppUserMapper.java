package com.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.AppUser;
import org.springframework.jdbc.core.RowMapper;

public class AppUserMapper implements RowMapper<AppUser> {

    public static final String BASE_SQL //
            = "Select u.User_Id, u.User_Name, u.User_Password From App_User u ";

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        Long userId = rs.getLong("User_Id");
        String userName = rs.getString("User_Name");
        String Password = rs.getString("User_Password");

        return new AppUser(userId, userName, Password);
    }

}