package com.example.dao;

import com.example.mapper.UserMapperNewDB;
import org.springframework.beans.factory.annotation.Autowired;
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


    public void setAvatar(String link, String loginedUser) {
        try {
            assert getJdbcTemplate() != null;
            getJdbcTemplate().update(UserMapperNewDB.SET_AVATAR_SQL, loginedUser, link);
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException in setAvatar!!!");
            npe.printStackTrace();
        }
    }

}