package com.example.mapper;

public class UserMapperNewDB {

    public static final String GET_OBJECT_ID_FOR_AVATAR = "" +
            "select params.object_id from params where params.value_text = ? and params.attribute_id = 3";

    public static final String GET_AVATAR_PATH = "" +
            "select params.value_text from params where params.object_id = ? and params.attribute_id = 5";

    public static final String ADD_AVATAR_SQL = "insert into params (object_id, attribute_id, object_references, value_text)\n" +
            "value (?, 5, \n" +
            "?, ?)";

    public static final String SET_AVATAR_SQL = "UPDATE params, \n" +
            "(select distinct params.object_id from params\n" +
            "join object on params.object_id = object.id\n" +
            "where params.value_text = ?) a,\n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id) b\n" +
            "SET params.value_text = ? \n" +
            "WHERE params.attribute_id = " +
            "(select attributes.id from attributes where attributes.Attribute = 'user_avatar')\n" +
            "AND params.object_id = a.object_id\n" +
            "AND params.attribute_id = b.attribute_id";

}
