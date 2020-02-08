package com.example.mapper;

public class NewMapperDB {

    public static final String CREATE_OBJECT_SQL ="insert into object(object_type_id, name) values\n" +
            "((select object_types.id from object_types where object_types.Object_Type = ?),?);";

    public static final String INSERT_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = ?), ?);\n";

    public static final String INSERT_ID_SQL = "insert into params (object_id, attribute_id, value_text) \n" +
            "value \n" +
            "((select id from object order by id desc limit 1), " +
            "(select attributes.id from attributes where attributes.Attribute = ?), " +
            "(select id from object order by id desc limit 1))";

    public static final String CREATE_OBJECT_REFERENCES = "update params\n" +
            "Set params.object_references = (select id from object order by id desc\n" +
            "limit 1)\n" +
            "where params.object_id = (select id from object order by id desc\n" +
            "limit 1)";

    public static final String CREATE_OBJECT_REFERENCES_FOR_NEW_ELEMENTS = "" +
            "update params\n " +
            "Set params.object_references = \n " +
            "(select * from (select params.object_id from params \n" +
            "where params.attribute_id = (select attributes.id from attributes where attributes.Attribute = ?) \n" +
            "and params.value_text = ?) as params1) " +
            "where params.attribute_id = (select attributes.id from attributes where attributes.Attribute = ?) " +
            "and params.value_text = ? " +
            "and params.object_references is NULL";

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

    public static final String SET_TIME_OF_LOGOUT_SQL = "UPDATE params,\n" +
            "(select distinct params.object_id from params\n" +
            "join object on params.object_id = object.id\n" +
            "where params.value_text = ?) a,\n" +
            "(select distinct params.object_id, params.value_text from params\n" +
            "join object on params.object_id = object.id\n" +
            "where params.value_text = ?\n" +
            "and params.attribute_id = (select attributes.id from attributes where attributes.Attribute = ?)) b,\n" +
            "(select distinct params.attribute_id from params\n" +
            "join attributes on params.attribute_id = attributes.id) c\n" +
            "SET params.value_text = ? \n" +
            "WHERE params.attribute_id =\n" +
            "(select attributes.id from attributes where attributes.Attribute = ?)\n" +
            "AND params.value_text = ?\n" +
            "AND params.object_id = a.object_id\n" +
            "AND params.object_id = b.object_id\n" +
            "AND params.attribute_id = c.attribute_id";

    public static final String ADD_SOMETHING_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values \n" +
            "((select object.id from object where object.id = \n" +
            "(select paparams.object_id from params as paparams where paparams.value_text = ? " +
            "and paparams.attribute_id = \n" +
            "(select attributes.id from attributes where attributes.Attribute = ?))),\n" +
            "(select attributes.id from attributes where attributes.Attribute = ?), ?)";

    public static final String DELETE_PARTICIPANT_FROM_EVENT = "" +
            "delete from params where\n" +
            "params.object_id = \n" +
            "(select a.object_id from\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_id')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as a,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_participant')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as b\n" +
            "where a.object_id = b.object_id)\n" +
            "and params.attribute_id = (select attributes.id from attributes where attributes.Attribute = 'event_participant')\n" +
            "and params.value_text = ?";

    public static final String DELETE_PARTICIPANT_FROM_CHAT = "" +
            "delete from params where\n" +
            "params.object_id = \n" +
            "(select a.object_id from\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_id')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as a,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_participant')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as b\n" +
            "where a.object_id = b.object_id)\n" +
            "and params.attribute_id = (select attributes.id from attributes where attributes.Attribute = 'chat_participant')\n" +
            "and params.value_text = ?";

    public static final String DELETE_CREATOR_FROM_CHAT = "" +
            "delete from params where\n" +
            "params.object_id = \n" +
            "(select a.object_id from\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_id')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as a,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_name_of_creator')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as b\n" +
            "where a.object_id = b.object_id)\n" +
            "and params.attribute_id = (select attributes.id from attributes where attributes.Attribute = 'chat_name_of_creator')\n" +
            "and params.value_text = ?";

    public static final String DELETE_SOMETHING_SQL = "delete from object where \n" +
            "object.id IN \n" +
            "(select params.object_id\n" +
            "from params, (select attributes.id as a from attributes \n" +
            "where \n" +
            "attributes.Attribute = ?) as a \n" +
            "where \n" +
            "params.attribute_id = a\n" +
            "and params.value_text = ?)";

}
