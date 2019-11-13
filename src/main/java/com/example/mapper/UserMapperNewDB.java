package com.example.mapper;

import com.example.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapperNewDB implements RowMapper<UserInfo>  {

    //public static final String BASE_SQL = "Select app_user.USER_ID, app_user.USER_NAME, app_user.USER_PASSWORD from app_user ";

    public static final String SELECT_ALL_EMAIL = "select params.value_text as email from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_email'\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)";

    public static final String SELECT_ALL_USERS = "select distinct A.value_text as email, B.value_text as name, C.value_text as password \n" +
            "from params as A, params as B, params as C\n" +
            "where A.value_text in (select params.value_text as email from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_email'\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "and B.value_text in (select params.value_text as email from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_name'\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "and C.value_text in (select params.value_text as email from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_password'\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "and A.object_id = B.object_id \n" +
            "and A.object_id = C.object_id";

    public static final String SELECT_ALL_PASSWORD = "select params.value_text as password from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_password'\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)\n";

    public static final String SELECT_EMAIL_NAME_PASSWORD_SQL = "SELECT DISTINCT (select params.value_text from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = 'user_email'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where params.attribute_id = \n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id\n" +
            "where attributes.Attribute = 'user_name')\n" +
            "and value_text = ?)) as email,\n" +
            "(select params.value_text from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_name'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where params.attribute_id = \n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id\n" +
            "where attributes.Attribute = 'user_name')\n" +
            "and value_text = ?)) as name,\n" +
            "(select params.value_text from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.Attribute = 'user_password'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where params.attribute_id = \n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id\n" +
            "where attributes.Attribute = 'user_name')\n" +
            "and value_text = ?)) as password\n" +
            "from params\n";

    public static final String SELECT_NAME_SQL = "select distinct params.value_text as name from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = 'user_name'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where \n" +
            "value_text = ?\n" +
            "and params.attribute_id = (select distinct attributes.id from attributes\n" +
            "where attributes.Attribute = 'user_name'))";

    public static final String SELECT_EMAIL_SQL = "" +
            "            select params.value_text as email from params\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            join object on params.object_id = object.id\n" +
            "            where attributes.Attribute = 'user_email'\n" +
            "            and object.id = \n" +
            "            (select distinct params.object_id from params where value_text = ?)";

    public static final String SELECT_IMAGE_SQL = "" +
            "            select params.value_text as image from params\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            join object on params.object_id = object.id\n" +
            "            where attributes.Attribute = 'user_avatar'\n" +
            "            and object.id = \n" +
            "            (select distinct params.object_id from params where value_text = ?)";

    public static final String SELECT_NAME_EMAIL_SQL = "SELECT DISTINCT (select params.value_text from params\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            join object on params.object_id = object.id\n" +
            "            where attributes.Attribute = 'user_email'\n" +
            "            and object.id = \n" +
            "            (select distinct params.object_id from params where value_text = ?)) as email,\n" +
            "            (select params.value_text from params\n" +
            "            join object on params.object_id = object.id\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            where attributes.Attribute = 'user_name'\n" +
            "            and object.id = \n" +
            "            (select distinct params.object_id from params where value_text = ?)) as name\n" +
            "            from params";

    public static final String SELECT_USER_ROLE ="select params.value_text as role from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = 'user_role'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where params.attribute_id = \n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id\n" +
            "where attributes.Attribute = 'user_name')\n" +
            "and value_text = ?)\n";

    public static final String SELECT_USER_AVATAR ="select params.value_blob as avatar from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = 'user_avatar'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where value_text = ?)";

    public static final String CREATE_OBJECT_SQL ="insert into object(object_type_id, name) values\n" +
            "            ((select object_types.id from object_types where object_types.Object_Type = 'users'),'OBJECT_USER');";

    public static final String INSERT_EMAIL_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "            values ((select id from object order by id desc\n" +
            "            limit 1),\n" +
            "            (select attributes.id from attributes where attributes.Attribute = 'user_email'), ?);\n";

    public static final String INSERT_NAME_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "            values ((select id from object order by id desc\n" +
            "            limit 1),\n" +
            "            (select attributes.id from attributes where attributes.Attribute = 'user_name'), ?);\n";

    public static final String INSERT_PASSWORD_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "            values ((select id from object order by id desc\n" +
            "            limit 1),\n" +
            "            (select attributes.id from attributes where attributes.Attribute = 'user_password'), ?);\n";

    public static final String INSERT_ROLE_SQL ="insert into params(object_id, attribute_id, value_text)\n" +
            "            values ((select id from object order by id desc\n" +
            "            limit 1),\n" +
            "            (select attributes.id from attributes where attributes.Attribute = 'user_role'), ?);";

    public static final String INSERT_AVATAR_SQL ="insert into params(object_id, attribute_id, value_blob)\n" +
            "            values ((select id from object order by id desc\n" +
            "            limit 1),\n" +
            "            (select attributes.id from attributes where attributes.Attribute = 'user_avatar'), '111');";
    public static final String CREATE_OBJECT_REFERENCES = "update params\n" +
            "Set params.object_references = (select id from object order by id desc\n" +
            "limit 1)\n" +
            "where params.object_id = (select id from object order by id desc\n" +
            "limit 1)";

    public static final String SET_PASSWORD_SQL = "UPDATE params, \n" +
            "(select distinct params.object_id from params\n" +
            "join object on params.object_id = object.id\n" +
            "where params.value_text = ?) a,\n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id) b\n" +
            "SET params.value_text = ? \n" +
            "WHERE params.attribute_id = " +
            "(select attributes.id from attributes where attributes.Attribute = 'user_password')\n" +
            "AND params.object_id = a.object_id\n" +
            "AND params.attribute_id = b.attribute_id";
    public static final String SET_AVATAR_SQL = "UPDATE params, \n" +
            "(select distinct params.object_id from params\n" +
            "join object on params.object_id = object.id\n" +
            "where params.value_text = ?) a,\n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id) b\n" +
            "SET params.value_blob = ? \n" +
            "WHERE params.attribute_id = " +
            "(select attributes.id from attributes where attributes.Attribute = 'user_avatar')\n" +
            "AND params.object_id = a.object_id\n" +
            "AND params.attribute_id = b.attribute_id";

    @Override
    public UserInfo mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        String email = resultSet.getString("email");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");

        return new UserInfo(email, name, password);
    }
}
