package com.example.dao;

import com.example.mapper.UserMapperNewDB;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@Transactional
public class NewUserDAO extends JdbcDaoSupport{

    @Autowired
    public NewUserDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public NewUserDAO(){}

    public List<UserInfo> getAccountsEmail() {
        String sql = UserMapperNewDB.SELECT_ALL_EMAIL;
        Object[] params = new Object[] {};
        UserMapperNewDB mapper = new UserMapperNewDB();
        return this.getJdbcTemplate().query(sql, params, mapper);
    }
    public List<UserInfo> getAccountsName() {
        String sql = UserMapperNewDB.SELECT_ALL_USERS;
        Object[] params = new Object[] {};
        UserMapperNewDB mapper = new UserMapperNewDB();
        return this.getJdbcTemplate().query(sql, params, mapper);
    }
    public List<UserInfo> getAccountsPassword() {
        String sql = UserMapperNewDB.SELECT_ALL_PASSWORD;
        Object[] params = new Object[] {};
        UserMapperNewDB mapper = new UserMapperNewDB();
        return this.getJdbcTemplate().query(sql, params, mapper);
    }

    public UserInfo findUserByName(String name) {
        String sql = UserMapperNewDB.SELECT_EMAIL_NAME_PASSWORD_SQL;
        Object[] params = new Object[] {name, name, name};
        UserMapperNewDB userMapper = new UserMapperNewDB();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, userMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserInfo findName(String name) {
        String sql = UserMapperNewDB.SELECT_NAME_SQL;
        Object[] params = new Object[] {name};

        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<UserInfo>() {
                @Override
                public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    String name = resultSet.getString("name");
                    return new UserInfo(name);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserInfo findEmail(String email) {
        String sql = UserMapperNewDB.SELECT_EMAIL_SQL;
        Object[] params = new Object[] {email};
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<UserInfo>() {
                @Override
                public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    String email1 = resultSet.getString("email");
                    return new UserInfo(email1);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /*public UserInfo findImageByName(String name) {
        String sql = UserMapperNewDB.SELECT_IMAGE_SQL;
        Object[] params = new Object[] {name};
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<UserInfo>() {
                @Override
                public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    String avatar = resultSet.getString("image");
                    return new UserInfo(avatar, avatar);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }*/


    public List<String> getUserRole(String name) {
        String sql = UserMapperNewDB.SELECT_USER_ROLE;
        Object[] params = new Object[] { name };
        return this.getJdbcTemplate().queryForList(sql, params, String.class);
    }

    /*public void insertUser(RegistrationForm registrationForm) throws NullPointerException{
        getJdbcTemplate().update(UserMapperNewDB.CREATE_OBJECT_SQL);
        getJdbcTemplate().update(UserMapperNewDB.INSERT_EMAIL_SQL, new Object[]{registrationForm.getEmail()});
        getJdbcTemplate().update(UserMapperNewDB.INSERT_NAME_SQL, new Object[]{registrationForm.getName()});
        getJdbcTemplate().update(UserMapperNewDB.INSERT_PASSWORD_SQL, new Object[]{registrationForm.getPassword()});
        getJdbcTemplate().update(UserMapperNewDB.INSERT_ROLE_SQL, new Object[]{registrationForm.getRole()});
        getJdbcTemplate().update(UserMapperNewDB.INSERT_AVATAR_SQL);
        getJdbcTemplate().update(UserMapperNewDB.CREATE_OBJECT_REFERENCES);
        new UserInfo();
    }*/

    /*public void changePassword(RegistrationForm registrationForm) throws NullPointerException{
        getJdbcTemplate().update(UserMapperNewDB.SET_PASSWORD_SQL, new Object[]{registrationForm.getName(),
                registrationForm.getPassword()});
    }*/

    public void setAvatar(String link, String loginedUser) {
        try {
            System.out.println("Name in setAvatar: " + loginedUser);
            System.out.println("link in setAvatar: " + link);
            getJdbcTemplate().update(UserMapperNewDB.SET_AVATAR_SQL, new Object[]{loginedUser, link});
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException in setAvatar!!!");
            npe.printStackTrace();
        }
    }

}