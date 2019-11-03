package com.example.mapper;

import com.example.model.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<AppUser> {

    public static final String BASE_SQL = "Select app_user.USER_ID, app_user.USER_NAME, app_user.USER_PASSWORD from app_user ";
    public static final String INSERT_SQL = "insert into app_user (USER_NAME, USER_PASSWORD, ENABLED) values ( ?, ?, ?)";
    public static final String SET_SQL = "INSERT INTO user_role (user_role.ROLE_ID) value (2)";
    public static final String SET_PASSWORD_SQL = "UPDATE app_user SET app_user.User_password = ? WHERE app_user.user_name = ?";
    public static final String SET_AVATAR_SQL = "UPDATE app_user SET app_user.User_avatar = ? WHERE app_user.user_name = ?";


    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        Long id = rs.getLong("USER_ID");
        String name = rs.getString("USER_NAME");
        String password = rs.getString("USER_PASSWORD");

        return new AppUser(id, name, password);
    }

}
