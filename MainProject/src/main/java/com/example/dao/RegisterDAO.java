package com.example.dao;

import com.example.form.RegistrationForm;
import com.example.mapper.UserMapper;
import com.example.model.AppUser;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;


@Repository
@Transactional
public class RegisterDAO extends JdbcDaoSupport{

    @Autowired
    public RegisterDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<AppUser> getAccounts() {
        String sql = UserMapper.BASE_SQL;

        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<AppUser> list = this.getJdbcTemplate().query(sql, params, mapper);
        return list;
    }

    public void insertUser(RegistrationForm registrationForm) throws NullPointerException{
        getJdbcTemplate().update(UserMapper.INSERT_SQL, new Object[]{registrationForm.getName(),
                registrationForm.getPassword(), registrationForm.getEnable()});
    }
    public void insertRole() throws NullPointerException{
        getJdbcTemplate().update(UserMapper.SET_SQL);
    }
    public void changePassword(RegistrationForm registrationForm) throws NullPointerException{
        getJdbcTemplate().update(UserMapper.SET_PASSWORD_SQL, new Object[]{registrationForm.getPassword(),
                registrationForm.getName()});
    }
    public void setAvatar(RegistrationForm registrationForm) throws NullPointerException{
        getJdbcTemplate().update(UserMapper.SET_AVATAR_SQL, new Object[]{registrationForm.getAvatar(),
                registrationForm.getName()});
    }

    public List<AppUser> getAvatar() {
        String sql = UserMapper.BASE_SQL;
        Object[] params = new Object[] {};
        UserMapper mapper = new UserMapper();
        List<AppUser> list = this.getJdbcTemplate().query(sql, params, mapper);
        return list;
    }
}
