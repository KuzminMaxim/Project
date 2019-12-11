package com.example.dao;

import com.example.api.Attribute;
import com.example.form.EventForm;
import com.example.mapper.NewMapperDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
public class NewDAO extends JdbcDaoSupport {

    @Autowired
    public NewDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public NewDAO() {}

    public void createSomething(Map attributesValues, String objectType) {
        try {
            assert getJdbcTemplate() != null;
            switch (objectType) {
                case "users":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, objectType, "OBJECT_USER");
                    break;
                case "event":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, objectType, "OBJECT_EVENT");
                    getJdbcTemplate().update(NewMapperDB.INSERT_DATE_OF_CREATION_SQL);
                    break;
                case "chat":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, objectType, "OBJECT_CHAT");
                    getJdbcTemplate().update(NewMapperDB.INSERT_DATE_OF_CHAT_CREATION_SQL);
                    break;
                case "messages":
                    getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, objectType, "OBJECT_MESSAGE");
                    break;
            }
            for ( Object entry : attributesValues.keySet()) {
                String key = (String) entry;
                String value = (String) attributesValues.get(key);
                if (value != null){
                    getJdbcTemplate().update(NewMapperDB.INSERT_SQL, key, value);
                }
            }
            getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES);
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void updateSomething(Map attributesValues, String objectType) {
        try {
            assert getJdbcTemplate() != null;
            if (objectType.equals("users")){
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("user_password")){
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                attributesValues.get("user_name"), attributesValues.get(key), key);
                    }
                }
            } else if (objectType.equals("event")){
                Date date = new Date();
                String currentTime = date.toString();
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("event_name")){
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                attributesValues.get("event_name"), "cancelled", "chat_status");
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                attributesValues.get("event_name"),
                                (attributesValues.get("event_name") + " was cancelled at " + currentTime), "chat_name");
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                attributesValues.get("event_name"),
                                (attributesValues.get("event_name") + " was cancelled at " + currentTime), "message_chat_name");
                    }
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void addSomething(Map attributesValues, String objectType) {
        try {
            assert getJdbcTemplate() != null;
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("event_participant")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("event_name"), "event_name", key, attributesValues.get(key));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "event_name", attributesValues.get("event_name"), "event_participant", attributesValues.get("event_participant"));
                    } else if (key.equals("chat_participant")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("chat_name"), "chat_name", key, attributesValues.get(key));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "chat_name", attributesValues.get("chat_name"), "chat_participant", attributesValues.get("chat_participant"));
                    }
                }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void deleteSomething(Map attributesValues) {

        try {
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("user_name")
                            && selectSomethingOne(EventForm.class, (String) attributesValues.get(key),
                            "event_name_of_creator") != null){
                        assert getJdbcTemplate() != null;
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                key, attributesValues.get(key));

                        EventForm eventForm = selectSomethingOne(EventForm.class, (String) attributesValues.get(key),
                                "event_name_of_creator");
                        System.out.println("eventForm.getNameOfEventCreator() in DELETE:" + eventForm.getNameOfEventCreator());

                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                "event_name_of_creator", eventForm.getNameOfEventCreator());
                    } else if (key.equals("user_name")
                            || key.equals("event_name")){
                        assert getJdbcTemplate() != null;
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                key, attributesValues.get(key));
                    }
                }
        } catch (NullPointerException | SQLException npe){
            npe.printStackTrace();
        }
    }

    public void deleteSomeoneFromSomething(Map attributesValues) {
        try {
            for ( Object entry : attributesValues.keySet()) {
                String key = (String) entry;
                if (key.equals("event_participant")){
                    assert getJdbcTemplate() != null;
                    getJdbcTemplate().update(NewMapperDB.DELETE_PARTICIPANT_FROM_EVENT,
                            attributesValues.get("event_name"), attributesValues.get(key), attributesValues.get(key));
                    getJdbcTemplate().update(NewMapperDB.DELETE_PARTICIPANT_FROM_CHAT,
                            attributesValues.get("event_name"), attributesValues.get(key), attributesValues.get(key));
                    getJdbcTemplate().update(NewMapperDB.DELETE_CREATOR_FROM_CHAT,
                            attributesValues.get("event_name"), attributesValues.get(key), attributesValues.get(key));
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }


    public <T> List <T> selectListOfSomething(Class clazz) throws IllegalAccessException, InstantiationException {

        List<T> list= new ArrayList<>();

        StringBuilder queryForResultSet = new StringBuilder("SELECT ");

        Field[] fields = clazz.getDeclaredFields();
        String[] names = new String[fields.length];
        String[] annotation = new String[fields.length];

        for (int i = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();
            exit:
            try {
                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException ignored){ continue; }
                    annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();

                if (names[i].equals("chatParticipant")
                        || names[i].equals("eventParticipant")
                            || names[i].equals("avatar")) {
                    //System.out.println(names[i] + "!");
                    break exit;
                } else {
                    queryForResultSet.append(annotation[i]).append(".value_text").append(" as ")
                            .append(fields[i].getName());
                    if (i < fields.length - 1){
                        queryForResultSet.append(", ");
                    } else {
                        queryForResultSet.append(" FROM ");
                    }
                }


            } catch (NullPointerException | NoSuchFieldException ignored){}
        }

        for (int i = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();
            exit:
            try {
                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException ignored){ continue; }

                    annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();

                if (names[i].equals("chatParticipant")
                        || names[i].equals("eventParticipant")
                            || names[i].equals("avatar")){
                    break exit;
                    //System.out.println(names[i] + "!");
                } else {
                    queryForResultSet.append("(select distinct params.value_text, params.object_id " +
                            "from params " +
                            "join object on params.object_id = object.id " +
                            "join attributes on attributes.id = params.attribute_id " +
                            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = ")
                            .append("'")
                            .append(annotation[i]).append("'").append(") and object.id IN " +
                            "(select distinct params.object_id from params)) as ").append(annotation[i]);
                    if (i < fields.length - 1){
                        queryForResultSet.append(", ");
                    } else {
                        queryForResultSet.append(" WHERE ");
                    }
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0, count = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();
            exit:
            try {
                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException ignored){ count += 1; continue; }

                annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();

                if (names[i].equals("chatParticipant")
                        || names[i].equals("eventParticipant")
                            || names[i].equals("avatar")){
                    break exit;
                    //System.out.println(names[i] + "!");
                } else {
                    queryForResultSet.append(annotation[count]).append(".object_id").append(" = ")
                            .append(annotation[i]).append(".object_id");
                    if (i < fields.length - 1){
                        queryForResultSet.append(" and ");
                    } else {
                        queryForResultSet.append(";");
                    }
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("queryForResultSet: " + queryForResultSet);

        try (Connection con = DataSourceUtils.getConnection(Objects.requireNonNull(getDataSource()));
             Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(String.valueOf(queryForResultSet));

            while(rs.next()) {
                try {
                    T newInstance = (T) clazz.getConstructor().newInstance();
                    loadResultIntoObject(rs, newInstance);
                    list.add(newInstance);
                } catch (NoSuchMethodException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }




        //System.out.println("listSize: " + list.size());
        return list;
    }

    public <T> List<T> selectListOfSomethingWhereSomething(Class clazz, String name, String attribute) throws SQLException {

        List<T> list= new ArrayList<T>();

        StringBuilder queryForResultSet = new StringBuilder("SELECT DISTINCT ");

        Field[] fields = clazz.getDeclaredFields();
        String[] names = new String[fields.length];
        String[] annotation = new String[fields.length];

        String fieldOfName = null;

        for (int i = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();

            exit:
            try {

                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException | NoSuchFieldException ignored) {
                    continue;
                }

                annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                if (annotation[i].equals(attribute)){
                    fieldOfName = names[i];
                }

                if ((names[i].equals("avatar") && !names[i].equals(fieldOfName))
                        || (names[i].equals("eventParticipant") && !names[i].equals(fieldOfName))
                        || (names[i].equals("chatParticipant") && !names[i].equals(fieldOfName))){
                    break exit;
                    //System.out.println(names[i] + "!");
                }else {
                    queryForResultSet.append(annotation[i]).append(".value_text").append(" as ").append(fields[i].getName());

                    if (i < fields.length - 1){
                        queryForResultSet.append(", ");
                    } else {
                        queryForResultSet.append(" FROM ");
                    }
                }
            } catch (NullPointerException | NoSuchFieldException ignored){}

        }

        for (int i = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();
            exit:
            try {
                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException ignored){ continue; }

                annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                if (annotation[i].equals(attribute)){
                    fieldOfName = names[i];
                }

                    if ((names[i].equals("avatar") && !names[i].equals(fieldOfName))
                        || (names[i].equals("eventParticipant") && !names[i].equals(fieldOfName))
                            || (names[i].equals("chatParticipant") && !names[i].equals(fieldOfName))){
                        break exit;
                        //System.out.println(names[i] + "!");
                    } else {
                    if (!annotation[i].equals(attribute)){
                            queryForResultSet.append("(select distinct params").append(".value_text")
                                    .append(", params.object_id " +
                                "from params " +
                                "join object on params.object_id = object.id " +
                                "join attributes on attributes.id = params.attribute_id " +
                                "where attributes.id = (select attributes.id from attributes where attributes.Attribute = ")
                                .append("'")
                                .append(annotation[i]).append("'").append(") and object.id IN " +
                                "(select distinct params.object_id from params)) as ").append(annotation[i]);
                    } else {
                        queryForResultSet.append("(select distinct params.value_text, params.object_id " +
                                "from params " +
                                "join object on params.object_id = object.id " +
                                "join attributes on params.attribute_id = attributes.id " +
                                "where attributes.id = (select attributes.id from attributes where attributes.Attribute = '")
                                .append(attribute).append("' and object.id IN " +
                                "(select distinct params.object_id from params) " +
                                "and params.value_text = '").append(name).append("')) as ").append(attribute);
                    }

                    if (i < fields.length - 1){
                        queryForResultSet.append(", ");
                    } else {
                        queryForResultSet.append(" WHERE ");
                    }
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0, count = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();
            exit:
            try {
                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException ignored){ count += 1; continue; }

                annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                if (annotation[i].equals(attribute)){
                    fieldOfName = names[i];
                }

                if ((names[i].equals("avatar") && !names[i].equals(fieldOfName))
                        || (names[i].equals("eventParticipant") && !names[i].equals(fieldOfName))
                        || (names[i].equals("chatParticipant") && !names[i].equals(fieldOfName))){
                    break exit;
                    //System.out.println(names[i] + "!");
                } else {
                    queryForResultSet.append(annotation[count]).append(".object_id").append(" = ")
                            .append(annotation[i]).append(".object_id");
                    if (i < fields.length - 1){
                        queryForResultSet.append(" and ");
                    } else {
                        queryForResultSet.append(";");
                    }
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("queryForResultSet: " + queryForResultSet);
        try (Connection con = DataSourceUtils.getConnection(Objects.requireNonNull(getDataSource()));
             Statement statement = con.createStatement()) {

            ResultSet rs = statement.executeQuery(String.valueOf(queryForResultSet));

            while (rs.next()) {
                try {
                    T newInstance = (T) clazz.getConstructor().newInstance();
                    loadResultIntoObject(rs, newInstance);
                    list.add(newInstance);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //System.out.println("listSize: " + list.size());

        return list;
    }

    public <T> T selectSomethingOne(Class clazz, String name, String attribute) throws SQLException {

        StringBuilder queryForResultSet = new StringBuilder("SELECT DISTINCT params.value_text as ");


        Field[] fields = clazz.getDeclaredFields();
        String[] names = new String[fields.length];
        String[] annotation = new String[fields.length];


        for (int i = 0; i < fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();


            try {

                try {
                    clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                } catch (NullPointerException | NoSuchFieldException ignored) {
                    continue;
                }
                annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();

                if (annotation[i].equals(attribute)){
                        queryForResultSet.append(fields[i].getName()).append(" FROM params " +
                                "join attributes on attributes.id = params.attribute_id " +
                                "join object on params.object_id = object.id " +
                                "where attributes.Attribute = '").append(annotation[i]).append("' and object.id = ")
                                .append("(select distinct params.object_id from params where value_text = '")
                                .append(name).append("' and params.attribute_id = (select distinct attributes.id from attributes ")
                                .append("where attributes.Attribute = '").append(annotation[i]).append("'))");
                    }

            } catch (NullPointerException | NoSuchFieldException ignored){}

        }
        //System.out.println(queryForResultSet);

        try (Connection con = DataSourceUtils.getConnection(Objects.requireNonNull(getDataSource()));
             Statement statement = con.createStatement()) {

            ResultSet rs = statement.executeQuery(String.valueOf(queryForResultSet));


            while(rs.next()) {
                try {
                    T newInstance = (T) clazz.getConstructor().newInstance();
                    loadResultIntoObject(rs, newInstance);
                    return newInstance;
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }



        return null;
    }

    private static void loadResultIntoObject(ResultSet rst, Object object) {
        Class<?> clazz = object.getClass();
        for(Field field : clazz.getDeclaredFields()) {
            String name = field.getName();
            field.setAccessible(true);
            try {
                if (field.getType() != Double.class){
                    Object value = rst.getObject(name);
                    field.set(object, value);
                } else {
                    Object value = rst.getDouble(name);
                    field.set(object, value);
                }

            } catch (IllegalAccessException | IllegalArgumentException e){
                e.printStackTrace();
            }
            catch (SQLException ignored){}
        }
    }

}
