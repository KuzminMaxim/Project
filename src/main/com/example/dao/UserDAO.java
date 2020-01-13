package com.example.dao;

import com.example.mapper.UserMapperNewDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;


@Repository
@Transactional
public class UserDAO extends JdbcDaoSupport{

    @Autowired
    public UserDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public UserDAO(){}

    public void addAvatar(String link, String userName) {
        try {
            assert getJdbcTemplate() != null;
            String objectId = getObjectId(userName);
            getJdbcTemplate().update(UserMapperNewDB.ADD_AVATAR_SQL, objectId, objectId, link);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setAvatar(String link, String userName) {
        try {
            assert getJdbcTemplate() != null;
            getJdbcTemplate().update(UserMapperNewDB.SET_AVATAR_SQL, userName, link);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getAvatarPath(String userName){
        String sql = UserMapperNewDB.GET_AVATAR_PATH;
        String objectId = getObjectId(userName);
        Object[] params = new Object[] {objectId};

        try {
            assert this.getJdbcTemplate() != null;
            return this.getJdbcTemplate().queryForObject(sql, params, (resultSet, i) -> resultSet.getString("value_text"));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private String getObjectId(String userName) {
        String sql = UserMapperNewDB.GET_OBJECT_ID_FOR_AVATAR;
        Object[] params = new Object[] {userName};

        try {
            assert this.getJdbcTemplate() != null;
            return this.getJdbcTemplate().queryForObject(sql, params, (resultSet, i) -> resultSet.getString("object_id"));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}