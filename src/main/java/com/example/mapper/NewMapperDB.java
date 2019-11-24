package com.example.mapper;

import com.example.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewMapperDB implements RowMapper<UserInfo> {

    public static final String CREATE_OBJECT_SQL ="insert into object(object_type_id, name) values\n" +
            "((select object_types.id from object_types where object_types.Object_Type = ?),?);";

    public static final String INSERT_DATE_OF_CREATION_SQL = "insert into params(object_id, attribute_id, value_date)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_date_of_creation'), CURDATE());";

    public static final String INSERT_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = ?), ?);\n";

    public static final String CREATE_OBJECT_REFERENCES = "update params\n" +
            "Set params.object_references = (select id from object order by id desc\n" +
            "limit 1)\n" +
            "where params.object_id = (select id from object order by id desc\n" +
            "limit 1)";

    public static final String SET_SOMETHING_SQL = "UPDATE params, \n" +
            "(select distinct params.object_id from params\n" +
            "join object on params.object_id = object.id\n" +
            "where params.value_text = ?) a,\n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id) b\n" +
            "SET params.value_text = ? \n" +
            "WHERE params.attribute_id = " +
            "(select attributes.id from attributes where attributes.Attribute = ?)\n" +
            "AND params.object_id = a.object_id\n" +
            "AND params.attribute_id = b.attribute_id";

    public static final String ADD_SOMETHING_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values \n" +
            "((select object.id from object where object.id = \n" +
            "(select paparams.object_id from params as paparams where paparams.value_text = ? " +
            "and paparams.attribute_id = \n" +
            "(select attributes.id from attributes where attributes.Attribute = ?))),\n" +
            "(select attributes.id from attributes where attributes.Attribute = ?), ?)";

    public static final String DELETE_SOMETHING_SQL = "delete from object where \n" +
            "object.id = \n" +
            "(select params.object_id from params where \n" +
            "params.attribute_id = \n" +
            "(select attributes.id from attributes where \n" +
            "attributes.Attribute = ?) \n" +
            "and params.value_text = ?)";

    public static final String SELECT_SOMETHING_SQL = "select distinct params.value_text as name from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = ?\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where \n" +
            "value_text = ?\n" +
            "and params.attribute_id = (select distinct attributes.id from attributes\n" +
            "where attributes.Attribute = ?))";

    @Override
    public UserInfo mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        String email = resultSet.getString("email");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");

        return new UserInfo(email, name, password);
    }
}
