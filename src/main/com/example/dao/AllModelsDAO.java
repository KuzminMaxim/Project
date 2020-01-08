package com.example.dao;

import com.example.api.Attribute;
import com.example.mapper.NewMapperDB;
import com.example.model.EventModel;
import com.example.model.UserLogoutChatModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class AllModelsDAO extends JdbcDaoSupport {

    @Autowired
    public AllModelsDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public AllModelsDAO() {}

    public void createNewObjectInDatabase(Map attributesValues, String objectType) {
        try {
            assert getJdbcTemplate() != null;

            getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_SQL, objectType, objectType);

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

    public void updateObjectInDatabase(Map attributesValues, String objectType) {
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
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("event_id")){
                        getJdbcTemplate().update(NewMapperDB.SET_SOMETHING_SQL,
                                attributesValues.get("event_id"), "cancelled", "chat_status");
                    }
                }
            } else if (objectType.equals("chat")){
                for (Object entry : attributesValues.keySet()){
                    String key = (String) entry;
                    if (key.equals("user_logout_chat_time")){
                        String timeOfLastLogout = "";
                        List<UserLogoutChatModel> list = getAllRecordsRelatedToThisClassObjectTypeAndThisNameIsAttribute(UserLogoutChatModel.class, (String) attributesValues.get("logout_user_name"), "logout_user_name");
                        for (UserLogoutChatModel userLogoutChatModel : list){
                            if (attributesValues.get("logout_chat_id").equals(userLogoutChatModel.getChatId())){
                                if (attributesValues.get("logout_user_name").equals(userLogoutChatModel.getUserName())){

                                    if (userLogoutChatModel.getUserLogoutTime().substring(24).equals(attributesValues.get("logout_user_name"))){
                                        timeOfLastLogout = userLogoutChatModel.getUserLogoutTime();
                                        break;
                                    }
                                }
                            }
                        }
                        getJdbcTemplate().update(NewMapperDB.SET_TIME_OF_LOGOUT_SQL,
                                attributesValues.get("logout_chat_id"), attributesValues.get("logout_user_name"), "logout_user_name",
                                attributesValues.get(key) + " " + attributesValues.get("logout_user_name"), key, timeOfLastLogout);
                    }
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void addOneNewAttributesIntoObjectInDatabase(Map attributesValues) {
        try {
            assert getJdbcTemplate() != null;
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("event_participant")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("event_id"), "event_id", key, attributesValues.get(key));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "event_id", attributesValues.get("event_id"), "event_participant", attributesValues.get("event_participant"));
                    } else if (key.equals("chat_participant")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("chat_id"), "chat_id", key, attributesValues.get(key));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "chat_id", attributesValues.get("chat_id"), "chat_participant", attributesValues.get("chat_participant"));
                    } else if (key.equals("user_logout_chat_time")){
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("logout_chat_id"), "chat_id", "logout_chat_id", attributesValues.get("logout_chat_id"));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "chat_id", attributesValues.get("logout_chat_id"), "logout_chat_id", attributesValues.get("logout_chat_id"));
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("logout_chat_id"), "chat_id", key, attributesValues.get(key) + " " + attributesValues.get("logout_user_name"));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "chat_id", attributesValues.get("logout_chat_id"), "user_logout_chat_time", attributesValues.get("user_logout_chat_time") + " " + attributesValues.get("logout_user_name"));
                        getJdbcTemplate().update(NewMapperDB.ADD_SOMETHING_SQL,
                                attributesValues.get("logout_chat_id"), "chat_id", "logout_user_name", attributesValues.get("logout_user_name"));
                        getJdbcTemplate().update(NewMapperDB.CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS,
                                "chat_id", attributesValues.get("logout_chat_id"), "logout_user_name", attributesValues.get("logout_user_name"));
                    }
                }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void deleteObjectFromDatabase(Map attributesValues) {

        try {
                for ( Object entry : attributesValues.keySet()) {
                    String key = (String) entry;
                    if (key.equals("user_name")
                            && findUniqueRecordInDatabaseWhereThisNameIsThisAttributeOfParticularObjectRelatedToThisClass
                            (EventModel.class, (String) attributesValues.get(key),
                            "event_name_of_creator") != null){
                        assert getJdbcTemplate() != null;
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                key, attributesValues.get(key));

                        EventModel eventModel = findUniqueRecordInDatabaseWhereThisNameIsThisAttributeOfParticularObjectRelatedToThisClass
                                (EventModel.class, (String) attributesValues.get(key),
                                "event_name_of_creator");

                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                "event_name_of_creator", eventModel.getNameOfEventCreator());
                    } else if (key.equals("user_id")
                            || key.equals("event_id")){
                        assert getJdbcTemplate() != null;
                        getJdbcTemplate().update(NewMapperDB.DELETE_SOMETHING_SQL,
                                key, attributesValues.get(key));
                    }
                }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public void deleteOneAttributeFromObjectInDatabase(Map attributesValues) {
        try {
            for ( Object entry : attributesValues.keySet()) {
                String key = (String) entry;
                if (key.equals("event_participant")){
                    assert getJdbcTemplate() != null;
                    getJdbcTemplate().update(NewMapperDB.DELETE_PARTICIPANT_FROM_EVENT,
                            attributesValues.get("event_id"), attributesValues.get(key), attributesValues.get(key));
                    getJdbcTemplate().update(NewMapperDB.DELETE_PARTICIPANT_FROM_CHAT,
                            attributesValues.get("event_id"), attributesValues.get(key), attributesValues.get(key));
                    getJdbcTemplate().update(NewMapperDB.DELETE_CREATOR_FROM_CHAT,
                            attributesValues.get("event_id"), attributesValues.get(key), attributesValues.get(key));
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }


    public <T> List <T> getAllRecordsFromTheDatabaseRelatedToThisClassObjectType(Class clazz) {

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
                            || names[i].equals("avatar")
                        || names[i].equals("id")) { //We do not select id, as this greatly slows down the speed of the request.
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
                            || names[i].equals("avatar")
                        || names[i].equals("id")){//We do not select id, as this greatly slows down the speed of the request.
                    break exit;
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
                            || names[i].equals("avatar")
                        || names[i].equals("id")){ //We do not select id, as this greatly slows down the speed of the request.
                    break exit;
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

        try (Connection con = DataSourceUtils.getConnection(Objects.requireNonNull(getDataSource()));
             Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(String.valueOf(queryForResultSet));

            while(rs.next()) {
                try {
                    T newInstance = (T) clazz.getConstructor().newInstance();
                    loadResultIntoObject(rs, newInstance);
                    list.add(newInstance);
                } catch (InstantiationException e){
                    e.printStackTrace();
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public <T> List<T> getAllRecordsRelatedToThisClassObjectTypeAndThisNameIsAttribute(Class clazz, String name, String attribute) {

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
                        || (names[i].equals("chatParticipant") && !names[i].equals(fieldOfName))
                        || names[i].equals("id") && !names[i].equals(fieldOfName)){//We do not select id, as this greatly slows down the speed of the request.
                    break exit;
                } else {
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
                            || (names[i].equals("chatParticipant") && !names[i].equals(fieldOfName))
                            || names[i].equals("id") && !names[i].equals(fieldOfName)){//We do not select id, as this greatly slows down the speed of the request.
                        break exit;
                    } else {
                    if (!annotation[i].equals(attribute)){
                            queryForResultSet.append("(select distinct params").append(".value_text")
                                    .append(", params.object_id " +
                                "from params " +
                                "inner join object on params.object_id = object.id " +
                                "inner join attributes on attributes.id = params.attribute_id " +
                                "where attributes.id = (select attributes.id from attributes where attributes.Attribute = ")
                                .append("'")
                                .append(annotation[i]).append("'").append(") and object.id IN " +
                                "(select distinct params.object_id from params)) as ").append(annotation[i]);
                    } else {
                        queryForResultSet.append("(select distinct params.value_text, params.object_id " +
                                "from params " +
                                "inner join object on params.object_id = object.id " +
                                "inner join attributes on params.attribute_id = attributes.id " +
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
                        || (names[i].equals("chatParticipant") && !names[i].equals(fieldOfName))
                        || names[i].equals("id") && !names[i].equals(fieldOfName)){//We do not select id, as this greatly slows down the speed of the request.
                    break exit;
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

        return list;
    }

    public <T> T findUniqueRecordInDatabaseWhereThisNameIsThisAttributeOfParticularObjectRelatedToThisClass(Class clazz, String name, String attribute) {

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
