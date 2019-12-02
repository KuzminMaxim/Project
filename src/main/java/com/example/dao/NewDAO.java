package com.example.dao;

import com.example.api.Attribute;
import com.example.api.ObjectType;
import com.example.mapper.NewMapperDB;
import com.example.mapper.UserMapperNewDB;
import com.example.model.UserInfo;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
            switch (objectType) {
                case "users":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, new Object[]{objectType, "OBJECT_USER"});
                    break;
                case "event":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, new Object[]{objectType, "OBJECT_EVENT"});
                    getJdbcTemplate().update(NewMapperDB.INSERT_DATE_OF_CREATION_SQL);
                    break;
                case "chat":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, new Object[]{objectType, "OBJECT_CHAT"});
                    getJdbcTemplate().update(NewMapperDB.INSERT_DATE_OF_CHAT_CREATION_SQL);
                    break;
                case "messages":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, new Object[]{objectType, "OBJECT_MESSAGE"});
                    break;
            }
            for ( Object entry : myMap.keySet()) {
                String key = (String) entry;
                String value = (String) myMap.get(key);
                if (value != null){
                    getJdbcTemplate().update(NewMapperDB.INSERT_SQL, new Object[]{key, value});
                }
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
            } else if (objectType.equals("event")){
                for ( Object entry : myMap.keySet()) {
                    String key = (String) entry;
                    if (key.equals("event_name")){
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                new Object[]{myMap.get("event_name"), "cancelled", "chat_status"});
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
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                new Object[]{"event_name", myMap.get("event_name"), "event_participant", myMap.get("event_participant")});
                    } else if (key.equals("chat_participant")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                new Object[]{ myMap.get("chat_name"), "chat_name", key, myMap.get(key)});
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                new Object[]{"chat_name", myMap.get("chat_name"), "chat_participant", myMap.get("chat_participant")});
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
                    if (key.equals("user_name")
                            && newEventDAO.findNameOfCreator((String) myMap.get(key)) != null){
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                new Object[]{key, myMap.get(key)});
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                new Object[]{"event_name_of_creator", newEventDAO.findNameOfCreator((String) myMap.get(key))});
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

    public void deleteSomeoneFromSomething(Map myMap) {
        try {
            for ( Object entry : myMap.keySet()) {
                String key = (String) entry;
                if (key.equals("event_participant")){
                    getJdbcTemplate().update(NewMapperDB.DELETE_PARTICIPANT_FROM_EVENT,
                            new Object[]{myMap.get("event_name"), myMap.get(key), myMap.get(key)});
                    getJdbcTemplate().update(NewMapperDB.DELETE_PARTICIPANT_FROM_CHAT,
                            new Object[]{myMap.get("event_name"), myMap.get(key), myMap.get(key)});
                    getJdbcTemplate().update(NewMapperDB.DELETE_CREATOR_FROM_CHAT,
                            new Object[]{myMap.get("event_name"), myMap.get(key), myMap.get(key)});
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public <T> List<T>  selectSomething(String objectType, Map myMap) {


            for (Object entry : myMap.keySet()){
                final String key = (String) entry;
                String fields = (String) myMap.get(key);

                System.out.println("Fields in select: " + fields);

                System.out.println("key:" + key);

            }

        List list = new ArrayList();

        String sql = NewMapperDB.SELECT_SOMETHING_SQL;

        Object[] params = new Object[] {"event_lng", "event_lng"};

        assert this.getJdbcTemplate() != null;
        String o = (String) this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet resultSet, int i) throws SQLException {
                String field = resultSet.getString("event_lng");
                list.add(field);
                return (T) list;
            }
        });
        System.out.println("List: " + list);

        System.out.println("List to array: " + Arrays.toString(list.toArray()));

        return  list;
    }


    /**
     *
     * @param <T>
     * @return
     * @throws NullPointerException
     */
    /*public <T>List<?> getAll() throws NullPointerException {
        String sql = NewMapperDB.SELECT_SOMETHING_SQL;
        Object params = new Object[] {};
        return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet resultSet, int i) throws SQLException {
                return null;
            }
        })
        return (List<SomeInfo>) new SomeInfo<T>();
    }*/


}
