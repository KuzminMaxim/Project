package com.example.mapper;

public class EventMapperNewDB {

    public static  final String SELECT_CANCELLED_CHATS= "" +
            "select distinct\n" +
            "chat_id.value_text as chat_id,\n" +
            "chat_name.value_text as chat_name,\n" +
            "chat_participant.value_text as chat_participant,\n" +
            "chat_status.value_text as chat_status\n" +
            "from\n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_id')\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))  as chat_id,\n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_name')\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))  as chat_name,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id\n" +
            "where attributes.id IN (select attributes.id from attributes where attributes.Attribute IN ('chat_name_of_creator', 'chat_participant') )\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)\n" +
            "and params.value_text = ?)\n" +
            "as chat_participant,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_status')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)\n" +
            "and params.value_text = 'cancelled')\n" +
            "as chat_status\n" +
            "where chat_id.object_id = chat_participant.object_id\n" +
            "and chat_id.object_id = chat_status.object_id\n" +
            "and chat_id.object_id = chat_name.object_id";

    public static final String FIND_COUNT_OF_PARTICIPANTS_FOR_THIS_EVENT = "" +
            "select count(event_participant.value_text) as participants\n" +
            "from\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_id')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as event_name,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id IN (select attributes.id from attributes where attributes.Attribute IN ('chat_participant', 'chat_name_of_creator'))\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))  as event_participant\n" +
            "where event_name.object_id = event_participant.object_id";

}
