package com.example.mapper;

public class UserMapperNewDB {

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
