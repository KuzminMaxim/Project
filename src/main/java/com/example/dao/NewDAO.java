package com.example.dao;

import com.example.mapper.NewMapperDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class NewDAO extends JdbcDaoSupport {

    @Autowired
    private NewEventDAO newEventDAO;

    @Autowired
    public NewDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public NewDAO() {}

    public void createSomething(Map myMap, String objectType) {
        try {
            assert getJdbcTemplate() != null;
            if (objectType.equals("users")){
                getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, new Object[]{objectType, "OBJECT_USER"});
            } else if (objectType.equals("event")){
                getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, new Object[]{objectType, "OBJECT_EVENT"});
                getJdbcTemplate().update(NewMapperDB.INSERT_DATE_OF_CREATION_SQL);
            }
            for ( Object entry : myMap.keySet()) {
                String key = (String) entry;
                String value = (String) myMap.get(key);
                getJdbcTemplate().update(NewMapperDB.INSERT_SQL, new Object[]{key, value});
            }
            getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES);
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void updateSomething(Map myMap, String objectType) {
        try {
            assert getJdbcTemplate() != null;
            if (objectType.equals("users")){
                for ( Object entry : myMap.keySet()) {
                    String key = (String) entry;
                    if (key.equals("user_password")){
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                new Object[]{myMap.get("user_name"), myMap.get(key), key});
                    }
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void addSomething(Map myMap, String objectType) {
        try {
            assert getJdbcTemplate() != null;
                for ( Object entry : myMap.keySet()) {
                    String key = (String) entry;
                    if (key.equals("event_participant")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                new Object[]{ myMap.get("event_name"), "event_name", key, myMap.get(key)});
                    }
                }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void deleteSomething(Map myMap) {
        try {
                for ( Object entry : myMap.keySet()) {
                    String key = (String) entry;
                    if (key.equals(("user_name"))
                            && newEventDAO.findNameOfCreator((String) myMap.get(key)) != null){
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                new Object[]{key, myMap.get(key)});
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                new Object[]{"event_name_of_creator", newEventDAO.findNameOfCreator((String) myMap.get(key))});
                        /**
                         * add deleting user from event !!!
                         */
                    } else if (key.equals("user_name")
                            || key.equals("event_name")){
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                new Object[]{key, myMap.get(key)});
                    }
                }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public List<?> getSomethingOne(){
        String sql = NewMapperDB.SELECT_SOMETHING_SQL;
        Object[] params = new Object[] {};
        return this.getJdbcTemplate().query(sql, params, new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("name");
            }
        });
    }


}
